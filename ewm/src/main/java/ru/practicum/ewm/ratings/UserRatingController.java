package ru.practicum.ewm.ratings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/ratings")
@Slf4j
@Validated
public class UserRatingController {

    private final RatingService ratingService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserRatingDto> findRatingForAuthor(@Positive @PathVariable Long userId) {
        UserRatingDto userRatingDto = ratingService.findRatingForAuthor(userId);
        return new ResponseEntity<>(userRatingDto, HttpStatus.OK);
    }
}
