package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {
    public User create(User user);

    public User update(User user, int id);

    public User get(int id);

    public void delete(int id);

}
