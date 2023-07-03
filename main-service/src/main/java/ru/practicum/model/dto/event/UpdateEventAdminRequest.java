package ru.practicum.model.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.Location;
import ru.practicum.constants.StateAction;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, max = 120)
    private String title;
}