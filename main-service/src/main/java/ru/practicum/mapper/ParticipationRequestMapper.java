package ru.practicum.mapper;

import lombok.Builder;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.dto.event.ParticipationRequestDto;

import java.time.LocalDateTime;

@Builder
public class ParticipationRequestMapper {

    public static ParticipationRequest inParticipation(Event event, User user) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .build();
    }

    public static ParticipationRequestDto inParticipationDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }
}