package ru.practicum.server.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private ForTimeBookingDto lastBooking;
    private ForTimeBookingDto nextBooking;
    private List<CommentResponseDto> comments;
}
