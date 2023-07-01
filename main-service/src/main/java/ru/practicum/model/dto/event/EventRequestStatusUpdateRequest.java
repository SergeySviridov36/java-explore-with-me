package ru.practicum.model.dto.event;

import lombok.*;
import ru.practicum.constants.StatusRequest;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;

    private StatusRequest status;
}