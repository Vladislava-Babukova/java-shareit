package ru.practicum.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.controller.dto.CreateUserDto;
import ru.practicum.server.user.controller.dto.UserDto;
import ru.practicum.server.user.controller.mapper.UserMapper;
import ru.practicum.server.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable int userId) {
        return userMapper.toDto(userService.get(userId));
    }

    @PostMapping
    public CreateUserDto create(@RequestBody CreateUserDto crUser) {
        return userMapper.toCreateDto(userService.create(userMapper.toUser(crUser)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable int userId,
                          @RequestBody UserDto userDto) {
        return userMapper.toDto(userService.update(userMapper.toUser(userDto), userId));

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable int userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
