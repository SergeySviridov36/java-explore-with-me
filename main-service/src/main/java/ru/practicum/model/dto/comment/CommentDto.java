package ru.practicum.model.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CommentDto {

    private Long id;

    private String userName;

    private Long eventId;

    private String text;

    private LocalDateTime created;
}