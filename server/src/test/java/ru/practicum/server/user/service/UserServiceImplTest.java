package ru.practicum.server.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.exceptions.ConflictException;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;
import ru.practicum.server.user.storage.mapper.UserRepositoryMapper;
import ru.practicum.server.user.storage.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserRepositoryMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("Test User");
        userEntity.setEmail("test@example.com");
    }

    @Test
    void create_ShouldSaveAndReturnUser() {
        when(mapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(mapper.toUser(any(UserEntity.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        verify(repository, times(1)).save(userEntity);
    }

    @Test
    void create_ShouldThrowConflictExceptionWhenSaveFails() {
        when(mapper.toEntity(any(User.class))).thenReturn(userEntity);
        when(repository.save(any(UserEntity.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(ConflictException.class, () -> userService.create(user));
    }

    @Test
    void update_ShouldUpdateAndReturnUser() {
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        when(repository.findById(anyInt())).thenReturn(Optional.of(userEntity));
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(mapper.toUser(any(UserEntity.class))).thenReturn(updatedUser);

        User result = userService.update(updatedUser, user.getId());

        assertNotNull(result);
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(mapper, times(1)).updateEntity(updatedUser, userEntity);
        verify(repository, times(1)).save(userEntity);
    }

    @Test
    void update_ShouldThrowDataNotFoundExceptionWhenUserNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());


        assertThrows(DataNotFoundException.class, () -> userService.update(user, 999));
    }

    @Test
    void get_ShouldReturnUserWhenExists() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(userEntity));
        when(mapper.toUser(any(UserEntity.class))).thenReturn(user);

        User foundUser = userService.get(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(repository, times(1)).findById(user.getId());
    }

    @Test
    void get_ShouldThrowDataNotFoundExceptionWhenUserNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.get(999));
    }

    @Test
    void delete_ShouldDeleteUser() {
        doNothing().when(repository).deleteById(anyInt());

        userService.delete(user.getId());

        verify(repository, times(1)).deleteById(user.getId());
    }

    @Test
    void delete_ShouldNotThrowWhenUserNotFound() {
        doNothing().when(repository).deleteById(anyInt());

        assertDoesNotThrow(() -> userService.delete(999));
    }
}