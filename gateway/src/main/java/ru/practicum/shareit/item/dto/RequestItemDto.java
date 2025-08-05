package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private Integer requestId;
}

