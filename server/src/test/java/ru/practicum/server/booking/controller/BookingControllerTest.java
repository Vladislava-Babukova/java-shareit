package ru.practicum.server.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.controller.dto.BookingCreateDto;
import ru.practicum.server.booking.controller.dto.BookingResponseDto;
import ru.practicum.server.booking.controller.mapper.BookingMapper;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingController bookingController;


    @Test
    void create_shouldReturnBookingResponseDto() {
        int userId = 1;
        BookingCreateDto createDto = new BookingCreateDto();
        Booking booking = new Booking();
        Booking createdBooking = new Booking();
        BookingResponseDto expectedDto = new BookingResponseDto();
        when(bookingMapper.toBooking(createDto, itemService)).thenReturn(booking);
        when(bookingService.create(booking, userId)).thenReturn(createdBooking);
        when(bookingMapper.toResponseDto(createdBooking)).thenReturn(expectedDto);

        BookingResponseDto result = bookingController.create(userId, createDto);


        assertSame(expectedDto, result);
        verify(bookingMapper).toBooking(createDto, itemService);
        verify(bookingService).create(booking, userId);
        verify(bookingMapper).toResponseDto(createdBooking);
    }

    @Test
    void updateBookingStatus_shouldReturnUpdatedBooking() {
        int bookingId = 1;
        int userId = 1;
        boolean approved = true;
        Booking updatedBooking = new Booking();
        BookingResponseDto expectedDto = new BookingResponseDto();

        when(bookingService.updateStatus(bookingId, userId, approved)).thenReturn(updatedBooking);
        when(bookingMapper.toResponseDto(updatedBooking)).thenReturn(expectedDto);

        BookingResponseDto result = bookingController.updateBookingStatus(bookingId, userId, approved);

        assertSame(expectedDto, result);
        verify(bookingService).updateStatus(bookingId, userId, approved);
        verify(bookingMapper).toResponseDto(updatedBooking);
    }

    @Test
    void getBookingInfo_shouldReturnBookingResponseDto() {
        int bookingId = 1;
        int userId = 1;
        Booking booking = new Booking();
        BookingResponseDto expectedDto = new BookingResponseDto();
        when(bookingService.getBookingInfo(bookingId, userId)).thenReturn(booking);
        when(bookingMapper.toResponseDto(booking)).thenReturn(expectedDto);

        BookingResponseDto result = bookingController.getBookingInfo(bookingId, userId);

        assertSame(expectedDto, result);
        verify(bookingService).getBookingInfo(bookingId, userId);
        verify(bookingMapper).toResponseDto(booking);
    }

    @Test
    void getAllBookings_shouldReturnListOfBookingResponseDto() {
        int userId = 1;
        State state = State.ALL;
        List<Booking> bookings = List.of(new Booking());
        List<BookingResponseDto> expectedList = List.of(new BookingResponseDto());
        when(bookingService.getAllBookings(userId, state)).thenReturn(bookings);
        when(bookingMapper.toResponseList(bookings)).thenReturn(expectedList);

        List<BookingResponseDto> result = bookingController.getAllBookings(userId, state);

        assertSame(expectedList, result);
        verify(bookingService).getAllBookings(userId, state);
        verify(bookingMapper).toResponseList(bookings);
    }

    @Test
    void getAllItemBookings_shouldReturnListOfBookingResponseDto() {
        int ownerId = 1;
        String state = "ALL";
        State bookingState = State.ALL;
        List<Booking> bookings = List.of(new Booking());
        List<BookingResponseDto> expectedList = List.of(new BookingResponseDto());
        when(bookingService.getAllItemBookings(ownerId, bookingState)).thenReturn(bookings);
        when(bookingMapper.toResponseList(bookings)).thenReturn(expectedList);

        List<BookingResponseDto> result = bookingController.getAllItemBookings(ownerId, state);

        assertSame(expectedList, result);
        verify(bookingService).getAllItemBookings(ownerId, bookingState);
        verify(bookingMapper).toResponseList(bookings);
    }


    @Test
    void create_whenInvalidData_shouldThrowValidationException() {
        int userId = 1;
        BookingCreateDto createDto = new BookingCreateDto();
        when(bookingMapper.toBooking(createDto, itemService)).thenThrow(new ValidationException("Invalid booking data"));


        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingController.create(userId, createDto));

        assertEquals("Invalid booking data", exception.getMessage());
        verifyNoInteractions(bookingService);
    }

    @Test
    void updateBookingStatus_whenBookingNotFound_shouldThrowDataNotFoundException() {
        int bookingId = 999;
        int userId = 1;
        boolean approved = true;
        when(bookingService.updateStatus(bookingId, userId, approved))
                .thenThrow(new DataNotFoundException("Booking not found"));


        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> bookingController.updateBookingStatus(bookingId, userId, approved));


        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void getBookingInfo_whenBookingNotFound_shouldThrowDataNotFoundException() {
        int bookingId = 999;
        int userId = 1;
        when(bookingService.getBookingInfo(bookingId, userId))
                .thenThrow(new DataNotFoundException("Booking not found"));


        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> bookingController.getBookingInfo(bookingId, userId));


        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void getAllItemBookings_whenInvalidState_shouldThrowValidationException() {
        int ownerId = 1;
        String invalidState = "INVALID_STATE";

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingController.getAllItemBookings(ownerId, invalidState));


        assertTrue(exception.getMessage().contains("INVALID_STATE"));
        verifyNoInteractions(bookingService, bookingMapper);
    }


    @Test
    void getAllBookings_whenNoBookings_shouldReturnEmptyList() {
        int userId = 1;
        State state = State.ALL;
        when(bookingService.getAllBookings(userId, state)).thenReturn(List.of());
        when(bookingMapper.toResponseList(List.of())).thenReturn(List.of());

        List<BookingResponseDto> result = bookingController.getAllBookings(userId, state);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllItemBookings_whenNoBookings_shouldReturnEmptyList() {
        int ownerId = 1;
        String state = "ALL";
        when(bookingService.getAllItemBookings(ownerId, State.ALL)).thenReturn(List.of());
        when(bookingMapper.toResponseList(List.of())).thenReturn(List.of());

        List<BookingResponseDto> result = bookingController.getAllItemBookings(ownerId, state);

        assertTrue(result.isEmpty());
    }
}