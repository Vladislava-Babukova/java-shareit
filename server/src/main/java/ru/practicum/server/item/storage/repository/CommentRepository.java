package ru.practicum.server.item.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.storage.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByItemId(int itemId);
}
