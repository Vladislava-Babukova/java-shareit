package ru.practicum.server.user.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.user.controller.dto.BookerDto;
import ru.practicum.server.user.controller.dto.CreateUserDto;
import ru.practicum.server.user.controller.dto.UserDto;
import ru.practicum.server.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserControllerMapperImpl userMapper;

    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {

        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("user@test.com");


        UserDto result = userMapper.toDto(user);


        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void toDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toDto(null));
    }

    @Test
    void toCreateDto_ShouldMapAllFieldsCorrectly() {

        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("user@test.com");


        CreateUserDto result = userMapper.toCreateDto(user);


        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void toCreateDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toCreateDto(null));
    }

    @Test
    void toUser_FromCreateUserDto_ShouldMapAllFieldsCorrectly() {

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setId(1);
        createUserDto.setName("Test User");
        createUserDto.setEmail("user@test.com");


        User result = userMapper.toUser(createUserDto);


        assertNotNull(result);
        assertEquals(createUserDto.getId(), result.getId());
        assertEquals(createUserDto.getName(), result.getName());
        assertEquals(createUserDto.getEmail(), result.getEmail());
    }

    @Test
    void toUser_FromCreateUserDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toUser((CreateUserDto) null));
    }

    @Test
    void toUser_FromUserDto_ShouldMapAllFieldsCorrectly() {

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("Test User");
        userDto.setEmail("user@test.com");


        User result = userMapper.toUser(userDto);


        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void toUser_FromUserDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toUser((UserDto) null));
    }

    @Test
    void toBookerDto_ShouldMapOnlyIdField() {

        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("user@test.com");


        BookerDto result = userMapper.toBookerDto(user);


        assertNotNull(result);
        assertEquals(user.getId(), result.getId());

    }

    @Test
    void toBookerDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(userMapper.toBookerDto(null));
    }

    @Test
    void toUser_FromCreateUserDto_ShouldHandleEmptyFields() {

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setId(1);


        User result = userMapper.toUser(createUserDto);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
    }

    @Test
    void toUser_FromUserDto_ShouldHandleEmptyFields() {

        UserDto userDto = new UserDto();
        userDto.setId(1);


        User result = userMapper.toUser(userDto);


        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getName());
        assertNull(result.getEmail());
    }
}