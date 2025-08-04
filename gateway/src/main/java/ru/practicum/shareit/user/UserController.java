package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(
            @PathVariable @Min(1) int userId) {
        log.info("Get user with id={}, requesterId={}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(
            @RequestBody @Valid CreateUserDto createUserDto) {
        log.info("Create new user by requesterId={}, data: {}", createUserDto);
        return userClient.createUser(createUserDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable @Min(1) int userId,
            @RequestBody @Valid UserDto userDto) {
        log.info("Update user with id={} by requesterId={}, new data: {}", userId, userDto);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable @Min(1) int userId) {
        log.info("Delete user with id={} by requesterId={}", userId);
        return userClient.deleteUser(userId);
    }
}

