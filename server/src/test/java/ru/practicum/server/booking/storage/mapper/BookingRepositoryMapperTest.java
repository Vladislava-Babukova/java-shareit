package ru.practicum.server.booking.storage.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.storage.entity.BookingEntity;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.item.storage.mapper.ItemRepositoryMapper;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.storage.entity.UserEntity;
import ru.practicum.server.user.storage.mapper.UserRepositoryMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryMapperTest {
    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @Mock
    private ItemRepositoryMapper itemRepositoryMapper;

    @InjectMocks
    private bookingRepositoryMapperImpl bookingRepositoryMapper;

    @Test
    void toBooking_ShouldMapCorrectly() {
        UserEntity bookerEntity = new UserEntity();
        bookerEntity.setId(1);
        bookerEntity.setName("Test User");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setName("Test Item");

        BookingEntity entity = new BookingEntity();
        entity.setId(1);
        entity.setStart(LocalDateTime.now().plusDays(1));
        entity.setEnd(LocalDateTime.now().plusDays(2));
        entity.setStatus(Status.APPROVED);
        entity.setBooker(bookerEntity);
        entity.setItem(itemEntity);

        User expectedBooker = new User();
        expectedBooker.setId(1);
        expectedBooker.setName("Test User");

        Item expectedItem = new Item();
        expectedItem.setId(1);
        expectedItem.setName("Test Item");

        when(userRepositoryMapper.toUser(bookerEntity)).thenReturn(expectedBooker);
        when(itemRepositoryMapper.toItem(itemEntity)).thenReturn(expectedItem);

        Booking result = bookingRepositoryMapper.toBooking(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getStart(), result.getStart());
        assertEquals(entity.getEnd(), result.getEnd());
        assertEquals(entity.getStatus(), result.getStatus());

        assertNotNull(result.getBooker());
        assertEquals(expectedBooker.getId(), result.getBooker().getId());
        assertEquals(expectedBooker.getName(), result.getBooker().getName());

        assertNotNull(result.getItem());
        assertEquals(expectedItem.getId(), result.getItem().getId());
        assertEquals(expectedItem.getName(), result.getItem().getName());

        verify(userRepositoryMapper).toUser(bookerEntity);
        verify(itemRepositoryMapper).toItem(itemEntity);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
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

        UserEntity expectedBookerEntity = new UserEntity();
        expectedBookerEntity.setId(1);
        expectedBookerEntity.setName("Test User");

        ItemEntity expectedItemEntity = new ItemEntity();
        expectedItemEntity.setId(1);
        expectedItemEntity.setName("Test Item");

        when(userRepositoryMapper.toEntity(booker)).thenReturn(expectedBookerEntity);
        when(itemRepositoryMapper.toEntity(item)).thenReturn(expectedItemEntity);

        BookingEntity result = bookingRepositoryMapper.toEntity(booking);

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getStatus(), result.getStatus());

        assertNotNull(result.getBooker());
        assertEquals(expectedBookerEntity.getId(), result.getBooker().getId());
        assertEquals(expectedBookerEntity.getName(), result.getBooker().getName());

        assertNotNull(result.getItem());
        assertEquals(expectedItemEntity.getId(), result.getItem().getId());
        assertEquals(expectedItemEntity.getName(), result.getItem().getName());

        verify(userRepositoryMapper).toEntity(booker);
        verify(itemRepositoryMapper).toEntity(item);
    }

    @Test
    void updateEntity_ShouldUpdateOnlyNonNullFields() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(3));
        booking.setEnd(LocalDateTime.now().plusDays(4));
        booking.setStatus(Status.REJECTED);

        BookingEntity entity = new BookingEntity();
        entity.setId(1);
        entity.setStart(LocalDateTime.now().plusDays(1));
        entity.setEnd(LocalDateTime.now().plusDays(2));
        entity.setStatus(Status.APPROVED);

        bookingRepositoryMapper.updateEntity(booking, entity);

        assertEquals(1L, entity.getId());
        assertEquals(booking.getStart(), entity.getStart());
        assertEquals(booking.getEnd(), entity.getEnd());
        assertEquals(booking.getStatus(), entity.getStatus());

        assertNull(entity.getBooker());
        assertNull(entity.getItem());
    }

    @Test
    void toBookingList_ShouldMapListCorrectly() {
        UserEntity bookerEntity = new UserEntity();
        bookerEntity.setId(1);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);

        BookingEntity entity1 = new BookingEntity();
        entity1.setId(1);
        entity1.setBooker(bookerEntity);
        entity1.setItem(itemEntity);

        BookingEntity entity2 = new BookingEntity();
        entity2.setId(2);
        entity2.setBooker(bookerEntity);
        entity2.setItem(itemEntity);

        List<BookingEntity> entityList = List.of(entity1, entity2);

        User expectedUser = new User();
        expectedUser.setId(1);

        Item expectedItem = new Item();
        expectedItem.setId(1);

        when(userRepositoryMapper.toUser(bookerEntity)).thenReturn(expectedUser);
        when(itemRepositoryMapper.toItem(itemEntity)).thenReturn(expectedItem);

        List<Booking> result = bookingRepositoryMapper.toBookingList(entityList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());

        verify(userRepositoryMapper, times(2)).toUser(bookerEntity);
        verify(itemRepositoryMapper, times(2)).toItem(itemEntity);
    }

    @Test
    void toBooking_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingRepositoryMapper.toBooking(null));
    }

    @Test
    void toEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingRepositoryMapper.toEntity(null));
    }

    @Test
    void updateEntity_ShouldDoNothingWhenSourceIsNull() {
        BookingEntity entity = new BookingEntity();
        entity.setId(1);

        bookingRepositoryMapper.updateEntity(null, entity);

        assertEquals(1L, entity.getId());
    }

    @Test
    void toBookingList_ShouldReturnNullWhenInputIsNull() {
        assertNull(bookingRepositoryMapper.toBookingList(null));
    }
}