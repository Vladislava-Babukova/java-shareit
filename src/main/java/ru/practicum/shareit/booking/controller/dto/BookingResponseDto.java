package ru.practicum.shareit.booking.controller.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.controller.dto.BookingItemDto;
import ru.practicum.shareit.user.controller.dto.BookerDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingItemDto item;
    private Status status;
    private BookerDto booker;
}
