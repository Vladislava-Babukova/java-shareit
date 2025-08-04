package ru.practicum.server.user.service;

import ru.practicum.server.user.model.User;

public interface UserService {
    public User create(User user);

    public User update(User user, int id);

    public User get(int id);

    public void delete(int id);

}
