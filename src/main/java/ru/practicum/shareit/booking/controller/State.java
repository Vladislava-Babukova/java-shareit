package ru.practicum.shareit.booking.controller;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State from
            (String value) {
        if (value == null || value.isBlank()) {
            return ALL;
        }
        try {
            return State.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Несуществующий статус " + value);
        }
    }
}
