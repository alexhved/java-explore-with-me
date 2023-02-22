package ru.practicum.ewm.participation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
    @Size(max = 20)
    private String status;
    @Positive
    private Long requester;
    @Positive
    private Long event;
}
