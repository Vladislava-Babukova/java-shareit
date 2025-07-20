package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {
    Item create(Item item, int userId);

    Item update(Item item, int itemId, int userId);

    Item get(int itemId);

    List<Item> getAll(int userId);

    List<Item> searchItems(String text);
}
