package ru.practicum.ewm.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity toEntity(UserShortDto userShortDto);

    UserDto toUserDto(UserEntity userEntity);

    UserShortDto toUserShortDto(UserEntity userEntity);
}
