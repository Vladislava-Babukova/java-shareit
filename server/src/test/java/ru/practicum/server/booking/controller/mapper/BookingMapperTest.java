package ru.practicum.server.booking.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.controller.dto.BookingCreateDto;
import ru.practicum.server.booking.controller.dto.BookingResponseDto;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.item.controller.dto.BookingItemDto;
import ru.practicum.server.item.controller.mapper.ItemMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.controller.dto.BookerDto;
import ru.practicum.server.user.controller.mapper.UserMapper;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    @Spy
    @InjectMocks
    private bookingControllerMapperImpl bookingMapper;

    @Test
    void toBooking_ShouldMapCorrectly() {
        int itemId = 1;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setItemId(itemId);
        createDto.setStart(start);
        createDto.setEnd(end);

        Item expectedItem = new Item();
        expectedItem.setId(itemId);
        when(itemService.get(itemId)).thenReturn(expectedItem);


        Booking result = bookingMapper.toBooking(createDto, itemService);


        assertNotNull(result);
        assertEquals(expectedItem, result.getItem());
        assertEquals(start, result.getStart());
        assertEquals(end, result.getEnd());
        assertNull(result.getStatus());

        verify(itemService).get(itemId);
    }

    @Test
    void toResponseDto_ShouldMapCorrectly() {
        User booker = new User();
        booker.setId(1);
        booker.setName("Test User");

        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.APPROVED);
        booking.setBooker(booker);
        booking.setItem(item);

        BookerDto bookerDto = new BookerDto(1);
        BookingItemDto itemDto = new BookingItemDto();
        itemDto.setId(1);
        itemDto.setName("Test Item");

        when(userMapper.toBookerDto(booker)).thenReturn(bookerDto);
        when(itemMapper.toBookingItemDto(item)).thenReturn(itemDto);

        BookingResponseDto result = bookingMapper.toResponseDto(booking);

        assertNotNull(result, "Метод toResponseDto вернул null");
        assertEquals(1, result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(Status.APPROVED, result.getStatus());

        assertNotNull(result.getBooker());
        assertEquals(1, result.getBooker().getId());

        assertNotNull(result.getItem());
        assertEquals(1, result.getItem().getId());
        assertEquals("Test Item", result.getItem().getName());

        verify(userMapper).toBookerDto(booker);
        verify(itemMapper).toBookingItemDto(item);
    }

    @Test
    void toResponseList_ShouldMapListCorrectly() {
        Booking booking1 = new Booking();
        booking1.setId(1);

        Booking booking2 = new Booking();
        booking2.setId(2);

        List<Booking> bookingList = List.of(booking1, booking2);


        List<BookingResponseDto> result = bookingMapper.toResponseList(bookingList);


        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void toBooking_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingMapper.toBooking(null, itemService));
    }

    @Test
    void toResponseDto_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingMapper.toResponseDto(null));
    }

    @Test
    void toResponseList_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingMapper.toResponseList(null));
    }
}