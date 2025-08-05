package ru.practicum.server.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForTimeBookingDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
