package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Data
public class Item {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private User owner;
    private Boolean available;
    private ItemRequest request;
}
