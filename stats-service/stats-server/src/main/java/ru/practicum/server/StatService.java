package ru.practicum.server;

import ru.practicum.dto.InputStatDto;
import ru.practicum.dto.OutputStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addStats(InputStatDto inputStat);

    List<OutputStatDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}