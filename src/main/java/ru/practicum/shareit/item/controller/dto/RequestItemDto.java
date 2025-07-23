package ru.practicum.shareit.item.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class RequestItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;


}

