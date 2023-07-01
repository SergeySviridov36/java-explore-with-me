package ru.practicum.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.api.repository.CompilationsRepository;
import ru.practicum.api.repository.EventRepository;
import ru.practicum.api.service.CompilationsService;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.dto.compilation.CompilationDto;
import ru.practicum.model.dto.compilation.NewCompilationDto;
import ru.practicum.model.dto.compilation.UpdateCompilationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.inCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null)
            compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        return CompilationMapper.inCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> findCompilationList(Boolean pinned, PageRequest pageRequest) {
        return compilationRepository.findAllByPinned(pinned, pageRequest)
                .stream()
                .map(CompilationMapper::inCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilation(Long compId) {
        return CompilationMapper.inCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена")));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest compilationRequest, Long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        if (compilationRequest.getEvents() != null)
            compilation.setEvents(eventRepository.findAllById(compilationRequest.getEvents()));
        return CompilationMapper.inCompilationDto(compilationRepository.save(CompilationMapper
                .inCompilation(compilationRequest, compilation)));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id))
            throw new NotFoundException("Подборка не найдена");
        compilationRepository.deleteById(id);
    }
}