package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.exceptions.ValidationException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State from(String value) {
        if (value == null || value.isBlank()) {
            return ALL;
        }
        try {
            return State.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Несуществующий статус " + value);
        }
    }
}
