package ru.practicum.server.user.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.user.controller.dto.BookerDto;
import ru.practicum.server.user.controller.dto.CreateUserDto;
import ru.practicum.server.user.controller.dto.UserDto;
import ru.practicum.server.user.model.User;

@Mapper(componentModel = "spring",
        implementationName = "UserControllerMapperImpl")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    @Mapping(target = "id", source = "id")
    CreateUserDto toCreateDto(User user);

    User toUser(CreateUserDto createUserDto);

    User toUser(UserDto userDto);

    BookerDto toBookerDto(User user);
}
