package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid RequestItemDto requestItemDto) {
        log.info("Creating item by userId={}, data: {}", userId, requestItemDto);
        return itemClient.createItem(userId, requestItemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid RequestItemDto requestItemDto) {
        log.info("Updating item {} by userId={}, data: {}", itemId, userId, requestItemDto);
        return itemClient.updateItem(itemId, userId, requestItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable int itemId) {
        log.info("Getting item {}", itemId);
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(
            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Getting all items for userId={}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam String text) {
        log.info("Searching items with text: {}", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid CommentCreateDto commentCreateDto) {
        log.info("Creating comment for item {} by userId={}", itemId, userId);
        return itemClient.createComment(itemId, userId, commentCreateDto);
    }
}
