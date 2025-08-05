package ru.practicum.server.request.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.request.storage.entity.RequestEntity;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
    List<RequestEntity> findAllByRequestorIdOrderByCreatedDesc(int requestorId);

    List<RequestEntity> findAllByOrderByCreatedDesc();
}
