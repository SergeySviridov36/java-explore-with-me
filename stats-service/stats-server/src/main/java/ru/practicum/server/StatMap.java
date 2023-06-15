package ru.practicum.server;

import ru.practicum.dto.InputStatDto;

public class StatMap {

    public static Stats inStats(InputStatDto inputStatDto) {
        Stats stats = new Stats();
        stats.setApp(inputStatDto.getApp());
        stats.setUri(inputStatDto.getUri());
        stats.setIp(inputStatDto.getIp());
        stats.setTimestamp(inputStatDto.getTimestamp());
        return stats;
    }
}