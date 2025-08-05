package ru.practicum.server.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.item.controller.dto.BookingItemDto;
import ru.practicum.server.user.controller.dto.BookerDto;

import java.time.LocalDateTime;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingItemDto item;
    private Status status;
    private BookerDto booker;
}
