package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputStatDto {

    private String app;
    private String uri;
    private Long hits;
}