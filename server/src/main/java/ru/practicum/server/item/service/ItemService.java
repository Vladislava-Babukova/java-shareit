package ru.practicum.server.item.service;

import ru.practicum.server.item.controller.dto.ItemResponseDto;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;

import java.util.List;


public interface ItemService {
    Item create(Item item, int userId);

    Item update(Item item, int itemId, int userId);

    ItemResponseDto getDto(int itemId);

    List<ItemResponseDto> getAll(int userId);

    List<Item> searchItems(String text);

    public Item get(int itemId);

    Comment createComment(Comment comment, int userId, int itemId);
}
