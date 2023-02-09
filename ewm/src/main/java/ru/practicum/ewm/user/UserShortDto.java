package ru.practicum.ewm.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserShortDto {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    @Email
    @Size(max = 30)
    private String email;
}
