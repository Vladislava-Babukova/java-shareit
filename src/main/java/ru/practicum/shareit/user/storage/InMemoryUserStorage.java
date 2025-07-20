package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();


    @Override
    public User create(User user) {
        if (checkUsers(user)) {
            checkEmail(user);
            users.put(user.getId(), user);
        }
        return user;
    }

    public Boolean checkUsers(User user) {
        if (users.containsKey(user.getId())) {
            return false;
        }
        return true;
    }

    public void checkEmail(User newUser) {
        if (newUser == null) {
            throw new ValidationException("Пользователь не может быть null");
        }
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(newUser.getEmail()));
        if (emailExists) {
            throw new ConflictException("Данный эмейл уже используется");
        }

    }

    @Override
    public User update(User user) {
        if (checkUsers(user)) {
            throw new DataNotFoundException("Пользователь с айди" + user.getId() + "не найден");
        }
        users.remove(user.getId());
        create(user);
        return user;
    }

    @Override
    public User get(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new DataNotFoundException("пользователь не найден");
        }
        return user;
    }

    @Override
    public void delete(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new DataNotFoundException("пользователь не найден");
        }
        users.remove(id);
    }
}
