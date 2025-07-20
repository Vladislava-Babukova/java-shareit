package ru.practicum.shareit.user.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.storage.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
