package ru.practicum.api.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.model.dto.event.*;
import ru.practicum.util.CurrentState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> findAllEvents(Long userId, PageRequest of);

    EventFullDto findEventById(Long userId, Long eventId);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest);

    List<EventFullDto> findAllEvents(List<Long> users, List<CurrentState> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    List<EventShortDto> findEvents(String text, Boolean paid, Boolean onlyAvailable, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest,
                                   HttpServletRequest request, String sort);

    EventFullDto findFullEventById(Long id, HttpServletRequest request);
}