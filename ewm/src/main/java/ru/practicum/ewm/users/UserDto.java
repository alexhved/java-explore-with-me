package ru.practicum.ewm.users;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private long id;
    private String name;
    private String email;
}
