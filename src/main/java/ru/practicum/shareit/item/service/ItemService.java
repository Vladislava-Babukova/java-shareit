package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.controller.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {
    Item create(Item item, int userId);

    Item update(Item item, int itemId, int userId);

    ItemResponseDto getDto(int itemId);

    List<ItemResponseDto> getAll(int userId);

    List<Item> searchItems(String text);

    public Item get(int itemId);

    Comment createComment(Comment comment, int userId, int itemId);
}
