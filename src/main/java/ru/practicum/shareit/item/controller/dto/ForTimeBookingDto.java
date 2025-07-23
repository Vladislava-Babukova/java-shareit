package ru.practicum.shareit.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ForTimeBookingDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
