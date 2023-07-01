package ru.practicum.mapper;

import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.dto.event.*;

import java.time.LocalDateTime;

import static ru.practicum.constants.CurrentState.PENDING;

public class EventMapper {

    public static EventFullDto inEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.inCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.inUserShortDto(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .title(event.getTitle())
                .views(0L)
                .build();
    }

    public static Event inEvent(NewEventDto newEventDto, User user, Category category) {
        return Event.builder()
                .eventDate(newEventDto.getEventDate())
                .category(category)
                .confirmedRequests(0)
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .initiator(user)
                .createdOn(LocalDateTime.now())
                .title(newEventDto.getTitle())
                .state(PENDING)
                .requestModeration(newEventDto.getRequestModeration())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .build();
    }

    public static EventShortDto inEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.inCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.inUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event inEventAdminUpdate(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getEventDate() != null)
            event.setEventDate(updateEventAdminRequest.getEventDate());
        if (updateEventAdminRequest.getAnnotation() != null)
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        if (updateEventAdminRequest.getDescription() != null)
            event.setDescription(updateEventAdminRequest.getDescription());
        if (updateEventAdminRequest.getPaid() != null)
            event.setPaid(updateEventAdminRequest.getPaid());
        if (updateEventAdminRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        if (updateEventAdminRequest.getEventDate() != null)
            event.setCreatedOn(updateEventAdminRequest.getEventDate());
        if (updateEventAdminRequest.getTitle() != null)
            event.setTitle(updateEventAdminRequest.getTitle());
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        return event;
    }

    public static Event inEventUpdate(Event event, UpdateEventUserRequest u) {
        UpdateEventAdminRequest updateEventAdminRequest = new UpdateEventAdminRequest(u.getAnnotation(), u.getCategory(),
                u.getDescription(), u.getEventDate(), u.getLocation(), u.getPaid(), u.getParticipantLimit(),
                u.getRequestModeration(), u.getStateAction(), u.getTitle());
        return inEventAdminUpdate(event, updateEventAdminRequest);
    }
}