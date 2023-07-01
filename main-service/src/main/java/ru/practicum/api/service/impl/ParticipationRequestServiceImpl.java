package ru.practicum.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.repository.EventRepository;
import ru.practicum.api.repository.ParticipationRequestRepository;
import ru.practicum.api.repository.UsersRepository;
import ru.practicum.api.service.ParticipationRequestService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.model.dto.event.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.CurrentState.PUBLISHED;
import static ru.practicum.util.StatusRequest.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {


    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UsersRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        checkEvents(userId, event);
        ParticipationRequest request = ParticipationRequestMapper.inParticipation(event, requester);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
        } else {
            request.setStatus(PENDING);
        }
        if (request.getStatus().equals(CONFIRMED))
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        return ParticipationRequestMapper.inParticipationDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (!eventRepository.existsById(eventId))
            throw new NotFoundException("Событие не найдено");
        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::inParticipationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findParticipationRequest(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationRequestMapper::inParticipationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto revokeParticipationRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        request.setStatus(CANCELED);
        return ParticipationRequestMapper.inParticipationDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new IllegalArgumentException("Лимит заявок 0");
        }
        List<Long> requestIds = request.getRequestIds();
        if (request.getStatus() == REJECTED) {
            return selectRejectedStatus(requestIds);
        } else if (request.getStatus() == CONFIRMED) {
            return selectConfirmedStatus(requestIds, event);
        } else
            throw new IllegalStateException("Статус указан не верно");
    }

    private EventRequestStatusUpdateResult selectRejectedStatus(List<Long> requestIds) {
        final List<ParticipationRequest> requests = requestRepository.findAllById(requestIds);
        validateStatus(requests);
        for (ParticipationRequest request : requests) {
            request.setStatus(REJECTED);
        }
        requestRepository.saveAll(requests);
        List<ParticipationRequestDto> rejected = requests
                .stream()
                .map(ParticipationRequestMapper::inParticipationDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(Collections.emptyList(), rejected);
    }

    private EventRequestStatusUpdateResult selectConfirmedStatus(List<Long> requestsIds, Event event) {
        int requests = event.getConfirmedRequests();
        int participantLimit = event.getParticipantLimit();
        if (requests == participantLimit && participantLimit > 0)
            throw new IllegalArgumentException("Достигнут лимит по заявкам на данное событие");
        List<ParticipationRequest> confirmedRequestsList;
        int balance = participantLimit - requests;
        if (requestsIds.size() > (balance)) {
            confirmedRequestsList = requestRepository.findAllById(requestsIds
                    .stream()
                    .limit(balance)
                    .collect(Collectors.toList()));
        } else {
            confirmedRequestsList = requestRepository.findAllById(requestsIds);
        }
        validateStatus(confirmedRequestsList);
        for (ParticipationRequest request : confirmedRequestsList) {
            request.setStatus(CONFIRMED);
            requests++;
        }
        List<ParticipationRequest> rejected = new ArrayList<>();
        List<Long> listIds = confirmedRequestsList.stream().map(ParticipationRequest::getId).collect(Collectors.toList());
        if (participantLimit == requests) {
            rejected = requestRepository.findAllByEventIdAndIdNotInAndStatus(event.getId(), listIds, PENDING)
                    .stream()
                    .peek(r -> r.setStatus(REJECTED))
                    .collect(Collectors.toList());
        }
        List<ParticipationRequest> updateRequests = new ArrayList<>(confirmedRequestsList);
        updateRequests.addAll(rejected);
        requestRepository.saveAll(updateRequests);
        event.setConfirmedRequests(requests);
        eventRepository.save(event);
        List<ParticipationRequestDto> resultList = confirmedRequestsList
                .stream()
                .map(ParticipationRequestMapper::inParticipationDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> participationRequestDtoList = rejected
                .stream()
                .map(ParticipationRequestMapper::inParticipationDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(resultList, participationRequestDtoList);
    }

    private void checkEvents(Long userId, Event event) {
        if (event.getInitiator().getId().equals(userId))
            throw new IllegalArgumentException("Нельзя себе отправить запрос");
        if (!event.getState().equals(PUBLISHED))
            throw new IllegalArgumentException("Нет события для участия");
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests().equals(event.getParticipantLimit()))
            throw new IllegalArgumentException("Превышен лимит запросов на участие в событии");
        if (requestRepository.existsByRequesterIdAndEventId(userId, event.getId()))
            throw new IllegalArgumentException("Запрос на событие уже существует");
    }

    private void validateStatus(List<ParticipationRequest> requests) {
        List<ParticipationRequest> requestsList = requests
                .stream()
                .filter(r -> r.getStatus().equals(CONFIRMED) || r.getStatus().equals(CANCELED) || r.getStatus().equals(REJECTED))
                .collect(Collectors.toList());
        if (!requestsList.isEmpty())
            throw new IllegalArgumentException("У заявки статус должен быть PENDING");
    }
}