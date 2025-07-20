package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateUserDto {
    private int id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
