package ru.practicum.ewm.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@Positive @PathVariable Long userId,
                                                 @Valid @RequestBody NewEventDto newEventDto) {

        EventFullDto eventFullDto = eventService.addEntity(userId, newEventDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }
}
