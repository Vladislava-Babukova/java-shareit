package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;


}

