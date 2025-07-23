package ru.practicum.shareit.item.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponseDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private ForTimeBookingDto lastBooking;
    private ForTimeBookingDto nextBooking;
    private List<CommentResponseDto> comments;
}
