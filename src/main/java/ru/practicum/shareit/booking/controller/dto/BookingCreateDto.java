package ru.practicum.shareit.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class BookingCreateDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
}
