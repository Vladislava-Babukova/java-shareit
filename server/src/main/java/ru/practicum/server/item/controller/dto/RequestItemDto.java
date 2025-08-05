package ru.practicum.server.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}

