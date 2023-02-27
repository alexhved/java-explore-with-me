package ru.practicum.ewm.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthorVsLikes {
    private Long authorId;
    private String author;
    private Long likes;
}
