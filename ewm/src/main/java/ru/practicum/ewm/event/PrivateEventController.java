package ru.practicum.ewm.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.participation.ParticipationRequestDto;
import ru.practicum.ewm.participation.ParticipationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationService participationService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@Positive @PathVariable Long userId,
                                                 @Valid @RequestBody NewEventDto newEventDto) {

        EventFullDto eventFullDto = eventService.addEvent(userId, newEventDto);
        log.info("add event : {}", newEventDto);
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@Positive @PathVariable Long userId,
                                                   @Positive @PathVariable Long eventId,
                                                   @RequestBody UpdateEventUserRequest updateEventUserRequest) {

        EventFullDto eventFullDto = eventService.updateEvent(userId, eventId, updateEventUserRequest);
        log.info("patch event : {}", updateEventUserRequest);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> findEventsByUser(@Positive @PathVariable Long userId,
                                                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        List<EventShortDto> eventShortDtoList = eventService.findEventsByUser(userId, from, size);
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> findOneByUser(@Positive @PathVariable Long userId,
                                                      @Positive @PathVariable Long eventId) {

        EventFullDto eventFullDto = eventService.findOneByUserAndEvent(userId, eventId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> findRequestsOnParticipationForUser(@Positive @PathVariable Long userId,
                                                                                            @Positive @PathVariable Long eventId) {

        List<ParticipationRequestDto> participationRequestDto = participationService.findRequestsOnParticipationForUser(userId, eventId);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }

    @PatchMapping("{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> changeStatusForParticipation(@Positive @PathVariable Long userId,
                                                                                       @Positive @PathVariable Long eventId,
                                                                                       @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {

        EventRequestStatusUpdateResult result = participationService.changeStatusForParticipation(userId, eventId, statusUpdateRequest);
        log.info("change status : {}", statusUpdateRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
