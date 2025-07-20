package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    Item get(int id);

    List<Item> getAll(int userId);

    List<Item> searchItems(String text);
}
