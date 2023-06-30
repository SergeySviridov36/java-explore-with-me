package ru.practicum.api.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.service.EventService;
import ru.practicum.api.service.ParticipationRequestService;
import ru.practicum.model.dto.event.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateUsersEventController {

    private final EventService eventService;
    private final ParticipationRequestService service;

    @GetMapping
    public List<EventShortDto> findAllEvents(@Positive @PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("Получен запрос GET /users/{userId}/events");
        return eventService.findAllEvents(userId, PageRequest.of(from > 0 ? from / size : 0, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto findEventById(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        log.debug("Получен запрос GET /users/{userId}/events/{eventId}");
        return eventService.findEventById(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.debug("Получен запрос POST /users/{userId}/events");
        return eventService.createEvent(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest eventUserRequest) {
        log.debug("Получен запрос PATH /users/{userId}/events/{eventId}");
        return eventService.updateUserEvent(userId, eventId, eventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsByEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        log.debug("Получен запрос PATH /users/{userId}/events/{eventId}/requests");
        return service.findRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.debug("Получен запрос PATH /users/{userId}/events/{eventId}/requests");
        return service.updateStatusRequests(userId, eventId, request);
    }
}