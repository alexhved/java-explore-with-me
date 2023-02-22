package ru.practicum.stats.mapper;

import dto.RequestStat;
import dto.ViewStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.stats.entity.StatEntity;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface StatMapper {

    @Mapping(target = "timestamp", source = "timestamp", qualifiedByName = "parseTimestamp")
    StatEntity toStatEntity(RequestStat requestStat);

    @Named("parseTimestamp")
    default LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    ViewStats toResponseStat(StatEntity statEntity);
}
