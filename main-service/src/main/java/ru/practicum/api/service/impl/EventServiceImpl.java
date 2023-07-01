package ru.practicum.api.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;
import ru.practicum.api.repository.CategoryRepository;
import ru.practicum.api.repository.EventRepository;
import ru.practicum.api.repository.UsersRepository;
import ru.practicum.api.service.EventService;
import ru.practicum.dto.OutputStatDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.User;
import ru.practicum.model.dto.event.*;
import ru.practicum.constants.CurrentState;
import ru.practicum.constants.StateAction;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.mapper.EventMapper.*;
import static ru.practicum.constants.CurrentState.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UsersRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new IllegalArgumentException("Дата и время события не может быть раньше," +
                    " чем два часа от текущего времени");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена."));
        Event event = inEvent(newEventDto, user, category);
        return inEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> findAllEvents(Long userId, PageRequest pageRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return eventRepository.findAllByInitiatorId(userId, pageRequest)
                .stream()
                .map(EventMapper::inEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> findAllEvents(List<Long> users, List<CurrentState> states, List<Long> categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest) {
        final BooleanExpression conditions = createForAdmin(users, states, categories, rangeStart, rangeEnd);
        return eventRepository.findAll(conditions, pageRequest)
                .stream()
                .map(EventMapper::inEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> findEvents(String text, Boolean paid, Boolean onlyAvailable, List<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest,
                                          HttpServletRequest request, String sort) {
        if (rangeStart != null || rangeEnd != null)
            if (rangeEnd.isBefore(rangeStart))
                throw new NumberFormatException("Дата старта раньше даты окончания");
        statClient.save(request);
        BooleanExpression conditions = createForPublic(text, paid, onlyAvailable, categories, rangeStart, rangeEnd);
        final List<EventShortDto> result = eventRepository.findAll(conditions, pageRequest)
                .stream()
                .map(EventMapper::inEventShortDto)
                .collect(Collectors.toList());
        final List<OutputStatDto> stats = statClient.get(LocalDateTime.now().minusDays(1222), LocalDateTime.now(), makeUris(result), true);
        return writeStats(result, sort, stats);
    }

    @Override
    public EventFullDto findFullEventById(Long id, HttpServletRequest request) {
        statClient.save(request);
        EventFullDto event = inEventFullDto(eventRepository.findByIdAndState(id, PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено")));
        OutputStatDto stat = statClient.get(LocalDateTime.now().minusDays(1222), LocalDateTime.now(),
                        List.of("/events/" + event.getId()), true)
                .stream()
                .findAny()
                .get();
        event.setViews(stat.getHits());
        return event;
    }

    @Override
    public EventFullDto findEventById(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return inEventFullDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено")));
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState().equals(PUBLISHED) || event.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new IllegalArgumentException("Статус не PENDING или CANCELED. Время на которые" +
                    " намечено событие не может быть раньше,чем через два часа от текущего момента");
        Event update = inEventUpdate(event, eventUserRequest);
        if (eventUserRequest.getStateAction() != null) {
            if (eventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW))
                update.setState(CANCELED);
            if (eventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW))
                update.setState(PENDING);
        }
        if (eventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(eventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена."));
            update.setCategory(category);
        }
        return inEventFullDto(eventRepository.save(update));
    }

    @Override
    @Transactional
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (event.getState().equals(PUBLISHED))
            throw new IllegalArgumentException("Событие уже опубликовано");
        Event eventUpdate = inEventAdminUpdate(event, updateEventAdminRequest);
        if (eventUpdate.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new IllegalArgumentException("Дата и время на которые намечено событие не может быть раньше,чем за час до времени публикации");
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT) && !event.getState().equals(PENDING))
                throw new IllegalArgumentException("Событие не должно быть PENDING");
            eventUpdate.setState(PUBLISHED);
            eventUpdate.setPublishedOn(LocalDateTime.now());
            if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT))
                eventUpdate.setState(REJECT);
        }
        return inEventFullDto(eventRepository.save(eventUpdate));
    }

    private BooleanExpression createForPublic(String text, Boolean paid, Boolean onlyAvailable,
                                              List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        QEvent qEvent = QEvent.event;
        BooleanExpression conditions = qEvent.state.eq(PUBLISHED);
        if (onlyAvailable)
            conditions.and(qEvent.participantLimit.ne(qEvent.confirmedRequests));
        if (categories != null)
            conditions.and(qEvent.category.id.in(categories));
        if (text != null) {
            conditions.and(qEvent.annotation.containsIgnoreCase(text));
            conditions.and(qEvent.description.containsIgnoreCase(text));
            conditions.and(qEvent.title.containsIgnoreCase(text));
        }
        if (paid != null)
            conditions.and(qEvent.paid.eq(paid));
        if (rangeStart != null && rangeEnd != null) {
            conditions.and(qEvent.eventDate.between(rangeStart, rangeEnd));
        } else {
            conditions.and(qEvent.eventDate.after(LocalDateTime.now()));
        }
        return conditions;
    }

    private BooleanExpression createForAdmin(List<Long> users, List<CurrentState> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        QEvent qEvent = QEvent.event;
        BooleanExpression conditions;
        if (users == null) {
            conditions = qEvent.initiator.id.notIn(new ArrayList<>());
        } else {
            conditions = qEvent.initiator.id.in(users);
        }
        if (categories != null)
            conditions.and(qEvent.category.id.in(categories));
        if (states != null)
            conditions.and(qEvent.state.in(states));
        if (rangeStart != null && rangeEnd != null)
            conditions.and(qEvent.eventDate.between(rangeStart, rangeEnd));
        return conditions;
    }

    private List<String> makeUris(List<EventShortDto> result) {
        return result
                .stream()
                .map(r -> "/events/" + r.getId())
                .collect(Collectors.toList());
    }

    private List<EventShortDto> writeStats(List<EventShortDto> result, String sort, List<OutputStatDto> stats) {
        Map<Long, Long> longMap = stats.stream().collect(Collectors.toMap((o -> inId(o.getUri())), (OutputStatDto::getHits)));
        result.forEach(e -> e.setViews(longMap.get(e.getId())));
        if (sort.equals("VIEWS"))
            return result
                    .stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        return result;
    }

    private Long inId(String url) {
        String[] uri = url.split("/");
        return Long.valueOf(uri[uri.length - 1]);
    }
}