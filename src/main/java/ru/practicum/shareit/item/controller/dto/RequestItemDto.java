package ru.practicum.shareit.item.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;


}

