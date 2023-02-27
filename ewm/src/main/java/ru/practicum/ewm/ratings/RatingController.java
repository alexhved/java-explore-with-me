package ru.practicum.ewm.ratings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/ratings")
@Slf4j
@Validated
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> addRate(@Positive @PathVariable Long userId,
                                        @Positive @PathVariable Long eventId,
                                        @NotNull @RequestParam Boolean isPositive) {
        ratingService.addRate(userId, eventId, isPositive);
        log.info("User with id: {} add rate: {} to event with id: {}", userId, isPositive, eventId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRate(@Positive @PathVariable Long userId,
                                           @Positive @PathVariable Long eventId) {
        ratingService.deleteRate(userId, eventId);
        log.info("User with id: {} delete rate from event with id: {}", userId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
