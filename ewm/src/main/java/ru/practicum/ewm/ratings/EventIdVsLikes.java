package ru.practicum.ewm.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventIdVsLikes {
    private Long eventId;
    private Long likes;
}
