package ru.practicum.model.dto.event;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.util.StatusRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ParticipationRequestDto {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long event;

    private Long requester;

    private StatusRequest status;
}