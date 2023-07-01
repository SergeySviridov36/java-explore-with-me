package ru.practicum.api.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.model.dto.compilation.CompilationDto;
import ru.practicum.model.dto.compilation.NewCompilationDto;
import ru.practicum.model.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationsService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(UpdateCompilationRequest compilationRequest, Long id);

    void deleteCompilation(Long id);

    List<CompilationDto> findCompilationList(Boolean pinned, PageRequest pageRequest);

    CompilationDto findCompilation(Long compId);
}