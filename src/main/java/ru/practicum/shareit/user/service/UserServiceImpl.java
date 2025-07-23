package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.entity.UserEntity;
import ru.practicum.shareit.user.storage.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.storage.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserRepositoryMapper mapper;
    private int id = 0;

    @Override
    public User create(User user) throws StorageException {
        try {
            return mapper.toUser(repository.save(mapper.toEntity(user)));
        } catch (Exception e) {
            throw new StorageException("Произошла ошибка при создании пользователя");
        }
    }

    @Override
    public User update(User user, int id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Не удалось найти пользователя"));
        mapper.updateEntity(user, entity);
        return mapper.toUser(repository.save(entity));

    }

    @Override
    public User get(int id) {
        return repository.findById(id)
                .map(mapper::toUser)
                .orElseThrow(() -> new DataNotFoundException("Не удалось найти пользователя"));
    }


    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }
}
