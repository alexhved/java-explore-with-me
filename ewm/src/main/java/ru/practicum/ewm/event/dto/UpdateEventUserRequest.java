package ru.practicum.ewm.event.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    protected String annotation;
    protected Integer category;
    protected String description;
    protected String eventDate;
    protected Location location;
    protected Boolean paid;
    protected Integer participantLimit;
    protected Boolean requestModeration;
    protected String stateAction;
    protected String title;
}
