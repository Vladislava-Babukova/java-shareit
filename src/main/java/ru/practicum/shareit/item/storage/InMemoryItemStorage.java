package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private Map<Integer, Item> itemMap = new HashMap<>();

    @Override
    public Item create(Item item) {
        checkItem(item);
        itemMap.put(item.getId(), item);
        return itemMap.get(item.getId());
    }

    public void checkItem(Item item) {
        if (item == null) {
            throw new ValidationException("item не должен быть null");
        }
    }

    public void checkForUpdate(Item item) {
        Item oldItem = itemMap.get(item.getId());
        if (oldItem.getOwner().getId() != item.getOwner().getId()) {
            throw new ConflictException("Изменять item может только его создатель");
        }
    }

    @Override
    public Item update(Item item) {
        checkItem(item);
        checkForUpdate(item);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(int id) {
        Item item = itemMap.get(id);
        if (item == null) {
            throw new DataNotFoundException("item не найден");
        }
        return item;
    }

    @Override
    public List<Item> getAll(int userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        String textLower = text.toLowerCase();
        return itemMap.values().stream()
                .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(textLower))
                .filter(item -> item.getAvailable() != false)
                .toList();
    }

}
