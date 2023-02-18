package ru.practicum.ewm.event;


import client.StatClient;
import dto.RequestStat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventSearchParamsDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {
    public static final String EWM_MAIN_SERVICE = "ewm-main-service";

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventService eventService;
    private final StatClient statClient;

    public PublicEventController(EventService eventService, StatClient statClient) {
        this.eventService = eventService;
        this.statClient = statClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> findById(@Positive @PathVariable Long id, HttpServletRequest servletRequest) {
        EventFullDto eventFullDto = eventService.findByIdAndStatePUBLISHED(id);

        RequestStat requestStat = buildRequestStatDto(servletRequest);

        statClient.save(requestStat);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> findWithParams
            (
                    @RequestParam(required = false) String text,
                    @RequestParam(required = false) List<Integer> categories,
                    @RequestParam(required = false) Boolean paid,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                    @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                    @RequestParam(required = false) SortingState sort,
                    @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                    @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
                    HttpServletRequest servletRequest
            ) {
        EventSearchParamsDto eventSearchParamsDto =
                new EventSearchParamsDto(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        List<EventShortDto> eventShortDtoList = eventService.findBySearchParamsForPublic(eventSearchParamsDto);

        RequestStat requestStat = buildRequestStatDto(servletRequest);

        statClient.save(requestStat);

        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    private static RequestStat buildRequestStatDto(HttpServletRequest servletRequest) {
        RequestStat requestStat = new RequestStat();
        requestStat.setApp(EWM_MAIN_SERVICE);
        requestStat.setUri(servletRequest.getRequestURI());
        requestStat.setIp(servletRequest.getRemoteAddr());
        requestStat.setTimestamp(LocalDateTime.now().format(DTF));
        return requestStat;
    }
}
