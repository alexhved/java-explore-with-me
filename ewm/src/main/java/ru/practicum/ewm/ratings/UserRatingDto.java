package ru.practicum.ewm.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRatingDto {
    private String authorName;
    private Integer rating;
}
