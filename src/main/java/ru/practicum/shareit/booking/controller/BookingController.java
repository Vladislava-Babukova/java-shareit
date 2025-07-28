package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.controller.dto.BookingCreateDto;
import ru.practicum.shareit.booking.controller.dto.BookingResponseDto;
import ru.practicum.shareit.booking.controller.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;
    private final BookingMapper mapper;
    private final ItemService itemService;

    @PostMapping
    public BookingResponseDto create(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestBody @Valid BookingCreateDto bookingCreateDto) {

        Booking booking = mapper.toBooking(bookingCreateDto, itemService);
        return mapper.toResponseDto(service.create(booking, userId));
    }


    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingStatus(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam boolean approved) {
        return mapper.toResponseDto(service.updateStatus(bookingId, userId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingInfo(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId) {

        return mapper.toResponseDto(service.getBookingInfo(bookingId, userId));
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", defaultValue = "ALL") State state) {
        List<Booking> bookingList = service.getAllBookings(userId, state);
        return mapper.toResponseList(bookingList);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllItemBookings(
            @RequestHeader("X-Sharer-User-Id") int owner,
            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        State bookingstate = State.from(state);
        List<Booking> bookingList = service.getAllItemBookings(owner, bookingstate);
        return mapper.toResponseList(bookingList);
    }
}
