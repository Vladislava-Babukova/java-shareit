package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper mapper;
    private final UserService userService;
    private int id = 0;

    @Override
    public Item create(Item item, int userId) {
        if (item.getAvailable() == null) {
            throw new ValidationException("доступность должна быть обозначена");
        }
        item.setOwner(userService.get(userId));
        item.setId(++id);
        return itemStorage.create(item);
    }

    @Override
    public Item update(Item item, int itemId, int userId) {
        item.setId(itemId);
        item.setOwner(userService.get(userId));
        return itemStorage.update(item);
    }

    @Override
    public Item get(int itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getAll(int userId) {
        return itemStorage.getAll(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<Item>();
        }
        return itemStorage.searchItems(text);
    }
}
