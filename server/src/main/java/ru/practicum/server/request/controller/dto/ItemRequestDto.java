package ru.practicum.server.request.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.user.controller.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private int id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
