package ru.practicum.shareit.user.controller.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;
    private String name;
    @Email
    private String email;


}
