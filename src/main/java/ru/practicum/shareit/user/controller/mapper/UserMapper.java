package ru.practicum.shareit.user.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.controller.dto.BookerDto;
import ru.practicum.shareit.user.controller.dto.CreateUserDto;
import ru.practicum.shareit.user.controller.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring",
        implementationName = "userControllerMapperImpl")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    @Mapping(target = "id", source = "id")
    CreateUserDto toCreateDto(User user);

    User toUser(CreateUserDto createUserDto);

    User toUser(UserDto userDto);

    BookerDto toBookerDto(User user);
}
