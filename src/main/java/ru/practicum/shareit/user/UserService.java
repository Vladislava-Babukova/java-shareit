package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;
    private int id = 0;

    public User create(User user) {
        user.setId(++id);
        return userStorage.create(user);
    }

    public User update(User user, int id) {
        user.setId(id);
        return userStorage.update(user);
    }

    public User get(int id) {
        return userStorage.get(id);
    }

    public void delete(int id) {
        userStorage.delete(id);
    }

}
