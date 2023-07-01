package ru.practicum.model.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewCommentDto {

    @NotBlank
    @Size(min = 1, max = 555)
    private String text;
}