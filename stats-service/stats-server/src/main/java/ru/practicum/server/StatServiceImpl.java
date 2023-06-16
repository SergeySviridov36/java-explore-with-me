package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.InputStatDto;
import ru.practicum.dto.OutputStatDto;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    @Transactional
    public void addStats(InputStatDto inputStat) {
        Stats stats = StatMap.inStats(inputStat);
        statRepository.save(stats);
    }

    @Override
    public List<OutputStatDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty()) {
            if (unique) {
                return statRepository.findByTimeIntervalUniqueIp(
                        start, end);
            } else {
                return statRepository.findByTimeInterval(
                        start, end);
            }
        } else {
            if (unique) {
                return statRepository.findByTimeIntervalAndListUniqueIp(
                        start, end, uris);
            } else {
                return statRepository.findByTimeIntervalAndList(
                        start, end, uris);
            }
        }
    }
}