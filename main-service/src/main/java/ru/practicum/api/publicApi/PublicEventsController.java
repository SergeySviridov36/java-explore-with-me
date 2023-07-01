package ru.practicum.api.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.service.EventService;
import ru.practicum.model.dto.event.EventFullDto;
import ru.practicum.model.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findEvents(@RequestParam(required = false) @Size(min = 1, max = 7000) String text,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                          LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size,
                                          HttpServletRequest request) {
        log.debug("Получен запрос GET /events");
        final PageRequest pageRequest;
        if (sort.equals("EVENT_DATE")) {
            final Sort sortList = Sort.by("eventDate").descending();
            pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, sortList);
        } else {
            pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        }
        return eventService.findEvents(text, paid, onlyAvailable, categories, rangeStart, rangeEnd, pageRequest, request, sort);
    }

    @GetMapping("/{id}")
    public EventFullDto findFullEventById(@PathVariable Long id,
                                          HttpServletRequest request) {
        log.debug("Получен запрос GET /events/{id}");
        return eventService.findFullEventById(id, request);
    }
}