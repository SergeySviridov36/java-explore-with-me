package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.InputStatDto;
import ru.practicum.dto.OutputStatDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatServerController {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStats(@RequestBody @Valid InputStatDto inputStat) {
        log.debug("Добавление статистики");
        statService.addStats(inputStat);
    }

    @GetMapping("/stats")
    public List<OutputStatDto> findStats(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime start,
                                         @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime end,
                                         @RequestParam(value = "uris", required = false, defaultValue = "")
                                         List<String> uris,
                                         @RequestParam(value = "unique", required = false, defaultValue = "false")
                                         Boolean unique) {
        log.debug("Получена статистика для списка uri: {}", uris);
        return statService.findStats(start, end, uris, unique);
    }
}