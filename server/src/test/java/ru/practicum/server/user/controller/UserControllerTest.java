package ru.practicum.server.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.user.controller.dto.CreateUserDto;
import ru.practicum.server.user.controller.dto.UserDto;
import ru.practicum.server.user.controller.mapper.UserMapper;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void get_shouldReturnUserDto() {

        int userId = 1;
        User user = new User();
        UserDto expectedDto = new UserDto();

        when(userService.get(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expectedDto);


        UserDto result = userController.get(userId);


        assertSame(expectedDto, result);
        verify(userService).get(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    void get_whenUserNotFound_shouldThrowException() {
        int userId = 999;
        when(userService.get(userId)).thenThrow(new DataNotFoundException("User not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userController.get(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userService).get(userId);
    }

    @Test
    void create_shouldReturnCreateUserDto() {
        CreateUserDto createUserDto = new CreateUserDto();
        User user = new User();
        User createdUser = new User();
        CreateUserDto expectedDto = new CreateUserDto();

        when(userMapper.toUser(createUserDto)).thenReturn(user);
        when(userService.create(user)).thenReturn(createdUser);
        when(userMapper.toCreateDto(createdUser)).thenReturn(expectedDto);


        CreateUserDto result = userController.create(createUserDto);


        assertSame(expectedDto, result);
        verify(userMapper).toUser(createUserDto);
        verify(userService).create(user);
        verify(userMapper).toCreateDto(createdUser);
    }

    @Test
    void create_whenInvalidData_shouldThrowException() {
        CreateUserDto invalidDto = new CreateUserDto();
        when(userMapper.toUser(invalidDto)).thenThrow(new ValidationException("Invalid data"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.create(invalidDto));

        assertEquals("Invalid data", exception.getMessage());
        verify(userMapper).toUser(invalidDto);
        verifyNoInteractions(userService);
    }

    @Test
    void update_shouldReturnUpdatedUserDto() {

        int userId = 1;
        UserDto userDto = new UserDto();
        User user = new User();
        User updatedUser = new User();
        UserDto expectedDto = new UserDto();

        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userService.update(user, userId)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);


        UserDto result = userController.update(userId, userDto);


        assertSame(expectedDto, result);
        verify(userMapper).toUser(userDto);
        verify(userService).update(user, userId);
        verify(userMapper).toDto(updatedUser);
    }

    @Test
    void update_whenUserNotFound_shouldThrowException() {
        int userId = 999;
        UserDto userDto = new UserDto();
        User user = new User();

        when(userMapper.toUser(userDto)).thenReturn(user);
        when(userService.update(user, userId)).thenThrow(new DataNotFoundException("User not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userController.update(userId, userDto));

        assertEquals("User not found", exception.getMessage());
        verify(userService).update(user, userId);
    }

    @Test
    void delete_shouldReturnNoContentResponse() {

        int userId = 1;

        ResponseEntity<Void> response = userController.delete(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).delete(userId);
    }

    @Test
    void delete_whenUserNotFound_shouldThrowException() {
        int userId = 999;
        doThrow(new DataNotFoundException("User not found")).when(userService).delete(userId);


        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userController.delete(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userService).delete(userId);
    }
}