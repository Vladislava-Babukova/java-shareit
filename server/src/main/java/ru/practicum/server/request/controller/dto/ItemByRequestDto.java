package ru.practicum.server.request.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemByRequestDto {
    private int id;
    @NotBlank
    private String name;
    private int owner;
}
