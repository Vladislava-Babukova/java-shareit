package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.dto.*;
import ru.practicum.shareit.item.controller.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid RequestItemDto requestItemDto) {

        Item item = itemMapper.toItem(requestItemDto);
        Item createdItem = itemService.create(item, userId);
        return itemMapper.toDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int userId,
                          @RequestBody RequestItemDto requestItemDto) {
        Item item = itemMapper.toItem(requestItemDto);
        Item updateItem = itemService.update(item, itemId, userId);
        return itemMapper.toDto(updateItem);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto get(@PathVariable int itemId) {
        return itemService.getDto(itemId);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam String text) {
        List<Item> foundItems = itemService.searchItems(text);
        return itemMapper.toDtoList(foundItems);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @PathVariable int itemId,
            @Valid @RequestBody CommentCreateDto createDto) {
        return itemMapper.toResponseComment(itemService.createComment(itemMapper.toComment(createDto), userId, itemId));
    }


}
