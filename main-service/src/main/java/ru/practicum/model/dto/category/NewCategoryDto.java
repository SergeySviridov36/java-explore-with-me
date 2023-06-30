package ru.practicum.model.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class NewCategoryDto {

    @Size(min = 1, max = 50)
    @NotBlank
    private String name;
}