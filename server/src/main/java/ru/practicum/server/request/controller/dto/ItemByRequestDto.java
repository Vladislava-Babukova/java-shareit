package ru.practicum.server.request.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemByRequestDto {
    private int id;
    private String name;
    private int owner;
}
