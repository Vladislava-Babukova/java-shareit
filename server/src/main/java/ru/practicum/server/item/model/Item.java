package ru.practicum.server.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private User owner;
    private Boolean available;
    private ItemRequest request;
}
