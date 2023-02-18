package ru.practicum.stats.controller;


import dto.RequestStat;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.stats.entity.StatEntity;
import ru.practicum.stats.mapper.StatMapper;
import ru.practicum.stats.service.StatService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class StatController {
    private final StatService statService;
    private final StatMapper statMapper;

    @PostMapping("/hit")
    public ResponseEntity<Void> save(@Valid @RequestBody RequestStat requestStat) {
        StatEntity statEntity = statMapper.toStatEntity(requestStat);
        statService.save(statEntity);
        log.info("StatService.save: " + requestStat.toString() + " /hit");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public List<ViewStats> find(@PositiveOrZero @RequestParam String start,
                                @Positive @RequestParam String end,
                                @RequestParam(required = false, defaultValue = "false") boolean unique,
                                @RequestParam String uris) {

        List<ViewStats> viewStats = statService.find(start, end, unique, uris);
        log.info("StatService.find: " + uris + " size - " + viewStats.size() + " /stats");
        return viewStats;
    }
}
