package ru.practicum.ewm.participation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Slf4j
@Validated
public class ParticipationController {
    private final ParticipationService participationService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipation(@Positive @PathVariable Long userId,
                                                                    @Positive @RequestParam Long eventId) {

        ParticipationRequestDto participationRequestDto = participationService.addParticipation(userId, eventId);
        log.info("add participation: {}", participationRequestDto);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> findParticipationByUser(@Positive @PathVariable Long userId) {

        List<ParticipationRequestDto> participationRequestDtoList = participationService.findByUserId(userId);
        return new ResponseEntity<>(participationRequestDtoList, HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipation(@Positive @PathVariable Long userId,
                                                                       @Positive @PathVariable Long requestId) {

        ParticipationRequestDto participationRequestDto = participationService.cancelParticipation(userId, requestId);
        log.info("cancel participation: {}", participationRequestDto);
        return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
    }
}
