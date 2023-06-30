package ru.practicum.model.dto.users;

import lombok.*;;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserShortDto {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}