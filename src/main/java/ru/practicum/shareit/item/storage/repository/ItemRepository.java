package ru.practicum.shareit.item.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.storage.entity.ItemEntity;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    List<ItemEntity> findByOwnerId(int userId);

    @Query("SELECT i FROM ItemEntity i WHERE " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR " +
            "LOWER(i.description) LIKE LOWER(CONCAT('%', :text2, '%'))) " +
            "AND i.available = true")
    List<ItemEntity> findAvailableByNameOrDescription(
            @Param("text") String text,
            @Param("text2") String text2);
}
