package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    @Mapping(target = "id", source = "id")
    CreateUserDto toCreateDto(User user);

    User toUser(CreateUserDto createUserDto);

    User toUser(UserDto userDto);
}
