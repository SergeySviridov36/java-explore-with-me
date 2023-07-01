package ru.practicum.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Location {

    private Float lat;

    private Float lon;
}