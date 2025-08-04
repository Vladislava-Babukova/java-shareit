package ru.practicum.server.request.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRequestDto {

    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemByRequestDto> items;
}
