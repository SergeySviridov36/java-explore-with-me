package ru.practicum.model.dto.compilation;

import lombok.*;
import ru.practicum.model.dto.event.EventShortDto;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CompilationDto {

    private Long id;
    private String title;
    private Boolean pinned;
    private Set<EventShortDto> events;
}