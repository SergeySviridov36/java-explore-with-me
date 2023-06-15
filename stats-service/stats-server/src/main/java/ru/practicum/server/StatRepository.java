package ru.practicum.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.OutputStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.practicum.dto.OutputStatDto(s.app, s.uri, count(distinct s.ip)) " +
            " FROM Stats as s " +
            " WHERE s.timestamp BETWEEN :start AND :end " +
            " GROUP BY s.app, s.uri ORDER BY count(s.id) DESC"
    )
    List<OutputStatDto> findByTimeIntervalUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.OutputStatDto(s.app, s.uri, count(s.ip)) " +
            " FROM Stats as s " +
            " WHERE s.timestamp BETWEEN :start AND :end " +
            " GROUP BY s.app, s.uri ORDER BY count(s.id) DESC"
    )
    List<OutputStatDto> findByTimeInterval(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.OutputStatDto(s.app, s.uri, count(distinct s.ip)) " +
            " FROM Stats as s " +
            " WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            " GROUP BY s.app, s.uri ORDER BY count(s.id) DESC"
    )
    List<OutputStatDto> findByTimeIntervalAndListUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.OutputStatDto(s.app, s.uri, count(s.ip)) " +
            " FROM Stats as s " +
            " WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            " GROUP BY s.app, s.uri ORDER BY count(s.id) DESC"
    )
    List<OutputStatDto> findByTimeIntervalAndList(LocalDateTime start, LocalDateTime end, List<String> uris);
}