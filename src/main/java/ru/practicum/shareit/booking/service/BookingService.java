package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.State;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking, int userId);

    Booking get(int id);

    Booking updateStatus(int bookingId, int userId, boolean approved);

    Booking getBookingInfo(int bookingId, int userId);

    List<Booking> getAllBookings(int userId, State state);

    List<Booking> getAllItemBookings(int userId, State state);
}
