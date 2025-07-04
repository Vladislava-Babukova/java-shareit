package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(int id);

    void delete(int id);
}
