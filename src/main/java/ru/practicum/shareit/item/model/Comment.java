package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class Comment {
    private int id;
    private String text;
    private Item item;
    private User author;
    private LocalDateTime created;
}
