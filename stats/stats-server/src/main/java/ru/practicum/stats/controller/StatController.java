package ru.practicum.stats.controller;


import dto.RequestStat;
import dto.ResponseStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.practicum.stats.entity.StatEntity;
import ru.practicum.stats.mapper.StatMapper;
import ru.practicum.stats.service.StatService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;
    private final StatMapper statMapper;

    @PostMapping("/hit")
    public void save(@RequestBody RequestStat requestStat) {
        StatEntity statEntity = statMapper.toStatEntity(requestStat);
        statService.save(statEntity);
    }

    @GetMapping("/stats")
    public List<ResponseStat> find(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false, defaultValue = "false") boolean unique,
                                   @RequestParam String uris) {
        return statService.find(start, end, unique, uris);
    }
}
