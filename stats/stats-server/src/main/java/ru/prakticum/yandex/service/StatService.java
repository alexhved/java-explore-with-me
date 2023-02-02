package ru.prakticum.yandex.service;

import dto.ResponseStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prakticum.yandex.entity.StatEntity;
import ru.prakticum.yandex.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;

    public void save(StatEntity statEntity) {
        statRepository.save(statEntity);
    }

    public List<ResponseStat> find(String start, String end, boolean unique, String uris) {
        String[] urisArray = uris.split(",");
        LocalDateTime startTime = parseTimestamp(start);
        LocalDateTime endTime = parseTimestamp(end);
        List<StatEntity> statEntityList;
        if (unique) {
            statEntityList = statRepository.findByTimeUnique(startTime, endTime, urisArray);
        } else {
            statEntityList = statRepository.findByTime(startTime, endTime, urisArray);
        }

        List<ResponseStat> responseStatList = new ArrayList<>(urisArray.length);
        for (String uri : urisArray) {
            responseStatList.add(new ResponseStat("ewm-main-service", uri, 0));
        }

        statEntityList.forEach(statEntity -> {
            for (ResponseStat responseStat : responseStatList) {
                if (statEntity.getUri().equals(responseStat.getUri())) {
                    int hits = responseStat.getHits();
                    responseStat.setHits(++hits);
                }
            }
        });

        return responseStatList.stream()
                .sorted(Comparator.comparing(ResponseStat::getHits).reversed()).collect(Collectors.toList());
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
