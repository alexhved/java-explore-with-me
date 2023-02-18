package ru.practicum.ewm.participation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    @Positive
    private Long id;
    @NotBlank
    private String created;
    @NotBlank
    private String status;
    @Positive
    private Long requester;
    @Positive
    private Long event;
}
