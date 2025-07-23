package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.controller.dto.BookingItemDto;
import ru.practicum.shareit.user.controller.dto.BookerDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponseDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingItemDto item;
    private Status status;
    private BookerDto booker;
}
