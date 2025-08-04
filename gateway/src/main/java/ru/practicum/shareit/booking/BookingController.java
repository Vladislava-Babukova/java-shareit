package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid BookingCreateDto bookingCreateDto) {
        log.info("Creating booking for user {}, booking: {}", userId, bookingCreateDto);
        return bookingClient.create(userId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam boolean approved) {
        log.info("Updating booking status {}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInfo(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Getting booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingInfo(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all bookings for user {}, state={}, from={}, size={}", userId, state, from, size);
        return bookingClient.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemBookings(
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Getting owner bookings for user {}, state={}, from={}, size={}", ownerId, state, from, size);
        return bookingClient.getAllItemBookings(ownerId, state, from, size);
    }
}
