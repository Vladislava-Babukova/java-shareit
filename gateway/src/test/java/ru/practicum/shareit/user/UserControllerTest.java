package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    private final int userId = 1;
    private CreateUserDto createUserDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto();
        createUserDto.setName("Test User");
        createUserDto.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated@example.com");
    }

    @Test
    void getUser_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.getUser(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getUser(userId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(userClient).getUser(userId);
    }

    @Test
    void createUser_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.createUser(createUserDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.createUser(createUserDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(userClient).createUser(createUserDto);
    }

    @Test
    void updateUser_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.updateUser(userId, userDto)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertThat(response).isEqualTo(expectedResponse);
        verify(userClient).updateUser(userId, userDto);
    }

    @Test
    void deleteUser_ShouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.deleteUser(userId)).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertThat(response).isEqualTo(expectedResponse);
        verify(userClient).deleteUser(userId);
    }

    @Test
    void getUser_ShouldNotThrowException_WhenUserIdIsZero() {
        int invalidUserId = 0;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.getUser(invalidUserId)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> userController.getUser(invalidUserId));
    }

    @Test
    void updateUser_ShouldNotThrowException_WhenUserIdIsNegative() {
        int invalidUserId = -1;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.updateUser(invalidUserId, userDto)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> userController.updateUser(invalidUserId, userDto));
    }

    @Test
    void deleteUser_ShouldNotThrowException_WhenUserIdIsZero() {
        int invalidUserId = 0;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.deleteUser(invalidUserId)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> userController.deleteUser(invalidUserId));
    }

    @Test
    void createUser_ShouldNotThrowException_WhenDtoIsInvalid() {
        CreateUserDto invalidDto = new CreateUserDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest().build();
        when(userClient.createUser(invalidDto)).thenReturn(expectedResponse);

        assertDoesNotThrow(() -> userController.createUser(invalidDto));
    }
}