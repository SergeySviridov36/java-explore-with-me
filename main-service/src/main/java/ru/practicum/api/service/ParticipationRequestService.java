package ru.practicum.api.service;

import ru.practicum.model.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.model.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.model.dto.event.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestDto> findRequestsByEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatusRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto revokeParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> findParticipationRequest(Long userId);
}