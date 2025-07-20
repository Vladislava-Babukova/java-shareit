package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.StorageException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.entity.UserEntity;
import ru.practicum.shareit.user.storage.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.storage.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceProvider implements UserService {

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
        try {
            UserEntity entity = repository.findById(id)
                    .orElseThrow(NotFoundException::new);
            mapper.updateEntity(user, entity);
            return mapper.toUser(repository.save(entity));
        } catch (NotFoundException e) {
            throw new DataNotFoundException("Не удалось найти пользователя");
        }
    }

    @Override
    public User get(int id) {
        try {
            return repository.findById(id)
                    .map(mapper::toUser)
                    .orElseThrow(NotFoundException::new);
        } catch (NotFoundException e) {
            throw new DataNotFoundException("Не удалось найти пользователя");
        }
    }


    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }
}
