package ru.practicum.mapper;

import ru.practicum.model.dto.compilation.CompilationDto;
import ru.practicum.model.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.dto.compilation.UpdateCompilationRequest;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation inCompilation(UpdateCompilationRequest request, Compilation compilation) {
        if (request.getTitle() != null)
            compilation.setTitle(request.getTitle());
        if (request.getPinned() != null)
            compilation.setPinned(request.getPinned());
        return compilation;
    }

    public static Compilation inCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                null,
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned(),
                null
        );
    }

    public static CompilationDto inCompilationDto(Compilation compilation) {
        final CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        if (compilation.getEvents() != null)
            compilationDto.setEvents(compilation.getEvents()
                    .stream()
                    .map(EventMapper::inEventShortDto)
                    .collect(Collectors.toSet()));
        return compilationDto;
    }
}