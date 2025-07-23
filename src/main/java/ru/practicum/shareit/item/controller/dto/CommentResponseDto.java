package ru.practicum.shareit.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
