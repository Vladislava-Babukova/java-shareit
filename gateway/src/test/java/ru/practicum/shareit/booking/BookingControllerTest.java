package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    private final int userId = 1;
    private final int bookingId = 1;
    private final int from = 0;
    private final int size = 10;

    @Test
    void create_shouldInvokeClientAndReturnResponse() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.create(eq(userId), any(BookingCreateDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = bookingController.create(userId, bookingCreateDto);


        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(bookingClient).create(eq(userId), any(BookingCreateDto.class));
    }

    @Test
    void updateBookingStatus_shouldInvokeClientAndReturnResponse() {
        boolean approved = true;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.updateStatus(eq(bookingId), eq(userId), eq(approved)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.updateBookingStatus(
                bookingId, userId, approved);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(bookingClient).updateStatus(eq(bookingId), eq(userId), eq(approved));
    }

    @Test
    void getBookingInfo_shouldInvokeClientAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.getBookingInfo(eq(bookingId), eq(userId)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = bookingController.getBookingInfo(bookingId, userId);


        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(bookingClient).getBookingInfo(eq(bookingId), eq(userId));
    }

    @Test
    void getAllBookings_shouldInvokeClientWithDefaultParamsAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.getAllBookings(eq(userId), eq(BookingState.ALL.toString()),
                eq(from), eq(size)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getAllBookings(
                userId, BookingState.ALL.toString(), from, size);


        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(bookingClient).getAllBookings(
                eq(userId), eq(BookingState.ALL.toString()), eq(from), eq(size));
    }

    @Test
    void getAllItemBookings_shouldInvokeClientWithDefaultParamsAndReturnResponse() {
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.getAllItemBookings(eq(userId), eq(BookingState.ALL.toString()),
                eq(from), eq(size)))
                .thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getAllItemBookings(
                userId, BookingState.ALL.toString(), from, size);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(bookingClient).getAllItemBookings(
                eq(userId), eq(BookingState.ALL.toString()), eq(from), eq(size));
    }
}