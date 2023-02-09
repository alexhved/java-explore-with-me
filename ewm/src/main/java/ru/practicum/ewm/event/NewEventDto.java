package ru.practicum.ewm.event;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
