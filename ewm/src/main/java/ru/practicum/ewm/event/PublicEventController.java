package ru.practicum.ewm.event;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stat_client.StatService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchParamsDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final EventService eventService;
    private final StatService statService;

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> findById(@Positive @PathVariable Long id, HttpServletRequest servletRequest) {
        EventFullDto eventFullDto = eventService.findByIdAndStatePUBLISHED(id);
        statService.saveStat(servletRequest);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> findWithParams(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortingState sort,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest servletRequest) {
        EventSearchParamsDto eventSearchParamsDto =
                new EventSearchParamsDto(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        List<EventShortDto> eventShortDtoList = eventService.findBySearchParamsForPublic(eventSearchParamsDto);

        statService.saveStat(servletRequest);

        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }
}
