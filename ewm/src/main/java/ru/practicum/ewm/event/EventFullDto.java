package ru.practicum.ewm.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.user.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private long id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration = true;
    private State state;
    private String title;
    private long views;
}
