package ru.practicum.stats.service;

import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.entity.StatEntity;
import ru.practicum.stats.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StatService {
    private final StatRepository statRepository;


    public void save(StatEntity statEntity) {
        statRepository.save(statEntity);
    }


    public List<ViewStats> find(String start, String end, boolean unique, String uris) {
        String[] urisArray = uris.split(",");
        LocalDateTime startTime = parseTimestamp(start);
        LocalDateTime endTime = parseTimestamp(end);
        List<ViewStats> viewStatsList;
        if (unique) {
            viewStatsList = statRepository.findByTimeUnique(startTime, endTime, urisArray);
        } else {
            viewStatsList = statRepository.findByTime(startTime, endTime, urisArray);
        }
        if (viewStatsList.isEmpty()) {
            viewStatsList = statRepository.findAllByTime(startTime, endTime);
        }

        return viewStatsList.stream()
                .sorted(Comparator.comparing(ViewStats::getHits).reversed()).collect(Collectors.toList());
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
