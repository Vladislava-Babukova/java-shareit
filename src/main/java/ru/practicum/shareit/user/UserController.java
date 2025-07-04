package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

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
    public User get(@PathVariable int userId) {
        return userService.get(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
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
