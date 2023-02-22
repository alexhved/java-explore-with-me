package ru.practicum.ewm.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminSearchParamsDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> moderateEvent(@Positive @PathVariable Long eventId,
                                                      @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        EventFullDto eventFullDto = eventService.moderateEvent(eventId, updateEventAdminRequest);
        log.info("moderate event with id: {} {}", eventId, updateEventAdminRequest);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> findForAdmin(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        AdminSearchParamsDto adminSearchParamsDto = new AdminSearchParamsDto(users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtoList = eventService.findBySearchParamsForAdmin(adminSearchParamsDto);
        return new ResponseEntity<>(eventFullDtoList, HttpStatus.OK);
    }
}
