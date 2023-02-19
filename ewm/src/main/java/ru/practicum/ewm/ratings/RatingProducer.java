package ru.practicum.ewm.ratings;

import org.springframework.stereotype.Component;

@Component
public class RatingProducer {
    public RatingDto calculate(Integer likes, Integer dislikes) {
        if (dislikes == 0 && likes == 0) {
            return new RatingDto(0, 0, 0);
        }

        int rating = (int) (likes / ((likes + dislikes) / 100f));
        return new RatingDto(likes, dislikes, rating);
    }
}
