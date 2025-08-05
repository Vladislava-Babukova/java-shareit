package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    public UserClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> getUser(int userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> createUser(CreateUserDto createUserDto) {
        return post("", createUserDto);
    }

    public ResponseEntity<Object> updateUser(int userId, UserDto userDto) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> deleteUser(int userId) {
        return delete("/" + userId);
    }
}
