package ru.practicum.server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.ConflictException;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.StorageException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;
import ru.practicum.server.user.storage.mapper.UserRepositoryMapper;
import ru.practicum.server.user.storage.repository.UserRepository;

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
            throw new ConflictException("Произошла ошибка при создании пользователя");
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
