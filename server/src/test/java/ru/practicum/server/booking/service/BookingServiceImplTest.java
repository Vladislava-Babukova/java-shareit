package ru.practicum.server.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.controller.State;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.storage.entity.BookingEntity;
import ru.practicum.server.booking.storage.mapper.BookingRepositoryMapper;
import ru.practicum.server.booking.storage.repository.BookingRepository;
import ru.practicum.server.exceptions.DataNotFoundException;
import ru.practicum.server.exceptions.ValidationException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private BookingRepositoryMapper mapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime future = now.plusDays(1);
    private final LocalDateTime past = now.minusDays(1);


    @Test
    void create_shouldSaveBookingWhenAllConditionsMet() {
        User booker = new User(1, "Booker", "booker@mail.com");
        Item item = new Item(1, "Item", "Description", new User(2, "Owner", "owner@mail.com"), true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, booker, Status.WAITING);
        BookingEntity entity = new BookingEntity();
        Booking savedBooking = new Booking(1, future, future.plusDays(1), item, booker, Status.WAITING);
        when(userService.get(1)).thenReturn(booker);
        when(mapper.toEntity(booking)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toBooking(entity)).thenReturn(savedBooking);

        Booking result = bookingService.create(booking, 1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(Status.WAITING, result.getStatus());
        verify(repository).save(entity);
    }

    @Test
    void create_shouldThrowWhenItemNotAvailable() {
        User booker = new User(1, "Booker", "booker@mail.com");
        Item item = new Item(1, "Item", "Description", new User(2, "Owner", "owner@mail.com"), false, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, booker, Status.WAITING);
        when(userService.get(1)).thenReturn(booker);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.create(booking, 1));

        assertEquals("Нельзя забронировать недоступный предмет", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void create_shouldThrowWhenEndBeforeStart() {
        User booker = new User(1, "Booker", "booker@mail.com");
        User owner = new User(2, "Owner", "owner@mail.com");
        Item item = new Item(1, "Item", "Description", owner, true, null);
        Booking booking = new Booking(1, future, past, item, booker, Status.WAITING);
        when(userService.get(1)).thenReturn(booker);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.create(booking, 1));

        assertEquals("Произошла ошибка при бронировании", exception.getMessage());
    }

    @Test
    void get_shouldReturnBookingWhenExists() {
        BookingEntity entity = new BookingEntity();
        Booking expected = new Booking(1, future, future.plusDays(1), new Item(), new User(), Status.WAITING);

        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(expected);

        Booking result = bookingService.get(1);

        assertEquals(expected, result);
    }

    @Test
    void get_shouldThrowWhenBookingNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> bookingService.get(1));

        assertEquals("Не удалось найти бронь", exception.getMessage());
    }


    @Test
    void updateStatus_shouldUpdateWhenOwnerApproves() {
        User owner = new User(1, "Owner", "owner@mail.com");
        Item item = new Item(1, "Item", "Description", owner, true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, new User(2, "Booker", "booker@mail.com"), Status.WAITING);
        BookingEntity entity = new BookingEntity();
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(booking);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toBooking(entity)).thenReturn(booking);

        Booking result = bookingService.updateStatus(1, 1, true);


        assertEquals(Status.APPROVED, result.getStatus());
        verify(repository).save(entity);
    }

    @Test
    void updateStatus_shouldThrowWhenNotOwner() {
        User owner = new User(1, "Owner", "owner@mail.com");
        Item item = new Item(1, "Item", "Description", owner, true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, new User(2, "Booker", "booker@mail.com"), Status.WAITING);
        BookingEntity entity = new BookingEntity();
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(booking);


        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.updateStatus(1, 2, true));

        assertEquals("Изменять статус может только владелец вещи", exception.getMessage());
        verify(repository, never()).save(any());
    }


    @Test
    void getBookingInfo_shouldReturnForOwner() {

        User owner = new User(1, "Owner", "owner@mail.com");
        Item item = new Item(1, "Item", "Description", owner, true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, new User(2, "Booker", "booker@mail.com"), Status.WAITING);
        BookingEntity entity = new BookingEntity();
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(booking);


        Booking result = bookingService.getBookingInfo(1, 1);


        assertEquals(booking, result);
    }

    @Test
    void getBookingInfo_shouldReturnForBooker() {
        User booker = new User(2, "Booker", "booker@mail.com");
        Item item = new Item(1, "Item", "Description", new User(1, "Owner", "owner@mail.com"), true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, booker, Status.WAITING);
        BookingEntity entity = new BookingEntity();
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(booking);

        Booking result = bookingService.getBookingInfo(1, 2);

        assertEquals(booking, result);
    }

    @Test
    void getBookingInfo_shouldThrowForUnauthorizedUser() {
        Item item = new Item(1, "Item", "Description", new User(1, "Owner", "owner@mail.com"), true, null);
        Booking booking = new Booking(1, future, future.plusDays(1), item, new User(2, "Booker", "booker@mail.com"), Status.WAITING);
        BookingEntity entity = new BookingEntity();

        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toBooking(entity)).thenReturn(booking);


        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.getBookingInfo(1, 3));

        assertEquals("Информация доступна только владельцу вещи или автору бронирования", exception.getMessage());
    }


    @Test
    void getAllBookings_shouldReturnAllForStateAll() {
        List<BookingEntity> entities = List.of(new BookingEntity());
        List<Booking> expected = List.of(new Booking());
        when(repository.findByBookerIdOrderByStartDesc(1)).thenReturn(entities);
        when(mapper.toBookingList(entities)).thenReturn(expected);

        List<Booking> result = bookingService.getAllBookings(1, State.ALL);

        assertEquals(expected, result);
    }

    @Test
    void getAllBookings_shouldReturnCurrent() {
        int userId = 1;
        State state = State.CURRENT;
        LocalDateTime now = LocalDateTime.now();

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        Booking booking = new Booking();
        List<Booking> expected = List.of(booking);

        doReturn(entities).when(repository)
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        eq(userId),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);

        List<Booking> result = bookingService.getAllBookings(userId, state);

        assertEquals(expected, result);
        verify(repository).findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                eq(userId),
                any(LocalDateTime.class),
                any(LocalDateTime.class));
        verify(mapper).toBookingList(entities);
    }

    @Test
    void getAllBookings_shouldReturnPast() {
        int userId = 1;
        State state = State.PAST;
        LocalDateTime now = LocalDateTime.now();

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByBookerIdAndEndBeforeOrderByStartDesc(eq(userId), any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);

        List<Booking> result = bookingService.getAllBookings(userId, state);


        assertEquals(expected, result);
        verify(repository).findByBookerIdAndEndBeforeOrderByStartDesc(eq(userId), any(LocalDateTime.class));
    }

    @Test
    void getAllBookings_shouldReturnFuture() {
        int userId = 1;
        State state = State.FUTURE;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByBookerIdAndStartAfterOrderByStartDesc(eq(userId), any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);

        List<Booking> result = bookingService.getAllBookings(userId, state);


        assertEquals(expected, result);
        verify(repository).findByBookerIdAndStartAfterOrderByStartDesc(eq(userId), any(LocalDateTime.class));
    }

    @Test
    void getAllBookings_shouldReturnWaiting() {
        int userId = 1;
        State state = State.WAITING;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByBookerIdAndStatusOrderByStartDesc(eq(userId), eq(Status.WAITING));

        doReturn(expected).when(mapper).toBookingList(entities);

        List<Booking> result = bookingService.getAllBookings(userId, state);


        assertEquals(expected, result);
        verify(repository).findByBookerIdAndStatusOrderByStartDesc(eq(userId), eq(Status.WAITING));
    }

    @Test
    void getAllBookings_shouldReturnRejected() {
        int userId = 1;
        State state = State.REJECTED;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByBookerIdAndStatusOrderByStartDesc(eq(userId), eq(Status.REJECTED));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllBookings(userId, state);


        assertEquals(expected, result);
        verify(repository).findByBookerIdAndStatusOrderByStartDesc(eq(userId), eq(Status.REJECTED));
    }

    @Test
    void getAllBookings_shouldReturnAll() {
        int userId = 1;
        State state = State.ALL;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByBookerIdOrderByStartDesc(eq(userId));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllBookings(userId, state);


        assertEquals(expected, result);
        verify(repository).findByBookerIdOrderByStartDesc(eq(userId));
    }


    @Test
    void getAllItemBookings_shouldReturnAllForStateAll() {
        List<BookingEntity> entities = List.of(new BookingEntity());
        List<Booking> expected = List.of(new Booking());
        User owner = new User(1, "Owner", "owner@mail.com");

        when(userService.get(1)).thenReturn(owner);
        when(repository.findAllByOwner(1)).thenReturn(entities);
        when(mapper.toBookingList(entities)).thenReturn(expected);


        List<Booking> result = bookingService.getAllItemBookings(1, State.ALL);


        assertEquals(expected, result);
    }

    @Test
    void getAllItemBookings_shouldReturnCurrent() {
        int ownerId = 1;
        State state = State.CURRENT;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());


        doReturn(entities).when(repository)
                .findCurrentByOwner(eq(ownerId), any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);


        assertEquals(expected, result);
        verify(repository).findCurrentByOwner(eq(ownerId), any(LocalDateTime.class));
    }

    @Test
    void getAllItemBookings_shouldReturnPast() {
        int ownerId = 1;
        State state = State.PAST;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findPastByOwner(eq(ownerId), any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);


        assertEquals(expected, result);
        verify(repository).findPastByOwner(eq(ownerId), any(LocalDateTime.class));
    }

    @Test
    void getAllItemBookings_shouldReturnFuture() {
        int ownerId = 1;
        State state = State.FUTURE;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findFutureByOwner(eq(ownerId), any(LocalDateTime.class));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);


        assertEquals(expected, result);
        verify(repository).findFutureByOwner(eq(ownerId), any(LocalDateTime.class));
    }

    @Test
    void getAllItemBookings_shouldReturnWaiting() {
        int ownerId = 1;
        State state = State.WAITING;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByOwnerAndStatus(eq(ownerId), eq(Status.WAITING));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);


        assertEquals(expected, result);
        verify(repository).findByOwnerAndStatus(eq(ownerId), eq(Status.WAITING));
    }

    @Test
    void getAllItemBookings_shouldReturnRejected() {
        int ownerId = 1;
        State state = State.REJECTED;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findByOwnerAndStatus(eq(ownerId), eq(Status.REJECTED));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);


        assertEquals(expected, result);
        verify(repository).findByOwnerAndStatus(eq(ownerId), eq(Status.REJECTED));
    }

    @Test
    void getAllItemBookings_shouldReturnAll() {
        int ownerId = 1;
        State state = State.ALL;

        BookingEntity entity = new BookingEntity();
        List<BookingEntity> entities = List.of(entity);
        List<Booking> expected = List.of(new Booking());

        doReturn(entities).when(repository)
                .findAllByOwner(eq(ownerId));

        doReturn(expected).when(mapper).toBookingList(entities);


        List<Booking> result = bookingService.getAllItemBookings(ownerId, state);

        assertEquals(expected, result);
        verify(repository).findAllByOwner(eq(ownerId));
    }
}