package ru.practicum.ewm.users;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity toEntity(NewUserRequest newUserRequest);

    UserDto toUserDto(UserEntity userEntity);

    UserShortDto toUserShortDto(UserEntity userEntity);
}
