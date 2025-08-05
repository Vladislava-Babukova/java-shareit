package ru.practicum.shareit.item;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    public ItemClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> createItem(int userId, RequestItemDto requestItemDto) {
        return post("", userId, requestItemDto);
    }

    public ResponseEntity<Object> updateItem(int itemId, int userId, RequestItemDto requestItemDto) {
        return patch("/" + itemId, userId, requestItemDto);
    }

    public ResponseEntity<Object> getItem(int itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getAllItems(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search", null, parameters);
    }

    public ResponseEntity<Object> createComment(int itemId, int userId, CommentCreateDto commentCreateDto) {
        return post("/" + itemId + "/comment", userId, commentCreateDto);
    }
}
