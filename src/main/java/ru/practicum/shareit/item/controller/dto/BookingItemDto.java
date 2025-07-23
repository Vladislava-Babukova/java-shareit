package ru.practicum.shareit.item.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingItemDto {
    private int id;
    @NotBlank
    private String name;
}
