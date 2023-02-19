package ru.practicum.ewm.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RatingDto {
    int likes;
    int dislikes;
    int rating;
}
