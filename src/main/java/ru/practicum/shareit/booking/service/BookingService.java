package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public interface BookingService {

    Booking create(Booking booking, int userId);

    Booking get(int id);

    Booking updateStatus(int bookingId, int userId, boolean approved);

    Booking getBookingInfo(int bookingId, int userId);

    List<Booking> getAllBookings(int userId, String state);

    List<Booking> getAllItemBookings(int userId, String state);
}
