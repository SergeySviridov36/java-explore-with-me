package ru.practicum.api.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}