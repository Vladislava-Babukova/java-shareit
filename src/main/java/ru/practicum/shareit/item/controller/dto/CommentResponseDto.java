package ru.practicum.shareit.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CommentResponseDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
