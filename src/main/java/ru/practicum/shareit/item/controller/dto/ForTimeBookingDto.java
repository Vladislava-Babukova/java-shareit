package ru.practicum.shareit.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ForTimeBookingDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
