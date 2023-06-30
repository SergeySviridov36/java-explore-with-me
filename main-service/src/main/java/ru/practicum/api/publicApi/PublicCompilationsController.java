package ru.practicum.api.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.service.CompilationsService;
import ru.practicum.model.dto.compilation.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {

    private final CompilationsService compilationService;

    @GetMapping
    public List<CompilationDto> findCompilationList(@RequestParam(defaultValue = "false") Boolean pinned,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        log.debug("Получен запрос GET /compilations");
        return compilationService.findCompilationList(pinned, pageRequest);
    }

    @GetMapping("/{compId}")
    public CompilationDto findCompilation(@PathVariable Long compId) {
        log.debug("Получен запрос GET /compilations/{compId}");
        return compilationService.findCompilation(compId);
    }
}