package ru.practicum.server.user.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.user.storage.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
