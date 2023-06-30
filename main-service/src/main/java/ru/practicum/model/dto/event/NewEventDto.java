package ru.practicum.model.dto.event;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.model.Location;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class NewEventDto {

    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    @NotNull
    private String title;
}