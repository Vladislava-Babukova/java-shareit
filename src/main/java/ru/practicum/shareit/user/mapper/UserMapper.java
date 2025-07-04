package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    User toUser(UserDto userDto);
}
