package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.entity.BookingEntity;
import ru.practicum.shareit.booking.storage.mapper.BookingRepositoryMapper;
import ru.practicum.shareit.booking.storage.repository.BookingRepository;
import ru.practicum.shareit.exceptions.DataNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceProvider implements BookingService {
    private final BookingRepositoryMapper mapper;
    private final BookingRepository repository;
    private final UserService userService;

    @Override
    public Booking create(Booking booking, int userId) {
        booking.setBooker(userService.get(userId));
        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Нельзя забронировать недоступный предмет");
        }
        try {

            checkStartTime(booking);
            BookingEntity entity = mapper.toEntity(booking);
            entity.setStatus(Status.WAITING);
            return mapper.toBooking(repository.save(entity));

        } catch (Exception e) {
            throw new ValidationException("Произошла ошибка при бронировании");
        }
    }

    private void checkStartTime(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Время окончания не может быть раньше старта");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время окончания не может быть в прошлом");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время старта не может быть в прошлом");
        }
    }

    @Override
    public Booking get(int id) {
        try {
            return repository.findById(id)
                    .map(mapper::toBooking)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new DataNotFoundException("Не удалось найти бронь");
        }
    }


    @Override
    public Booking updateStatus(int bookingId, int userId, boolean approved) {
        Booking booking = get(bookingId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Изменять статус может только владелец вещи");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        try {
            BookingEntity entity = repository.findById(bookingId).orElseThrow(ChangeSetPersister.NotFoundException::new);
            mapper.updateEntity(booking, entity);
            return mapper.toBooking(repository.save(entity));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new DataNotFoundException("не удалось найти бронь");
        }
    }

    @Override
    public Booking getBookingInfo(int bookingId, int userId) {
        Booking booking = get(bookingId);
        if (booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId) {
            return booking;
        } else {
            throw new ValidationException("Информация доступна только владельцу вещи или автору бронирования");
        }
    }

    @Override
    public List<Booking> getAllBookings(int userId, String state) {

        switch (state) {
            case "CURRENT":
                return mapper.toBookingList(repository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now()));
            case "PAST":
                return mapper.toBookingList(repository.findByBookerIdAndEndBeforeOrderByStartDesc(
                        userId, LocalDateTime.now()));
            case "FUTURE":
                return mapper.toBookingList(repository.findByBookerIdAndStartAfterOrderByStartDesc(
                        userId, LocalDateTime.now()));
            case "WAITING":
                return mapper.toBookingList(repository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, Status.WAITING));
            case "REJECTED":
                return mapper.toBookingList(repository.findByBookerIdAndStatusOrderByStartDesc(
                        userId, Status.REJECTED));
            default:
                return mapper.toBookingList(repository.findByBookerIdOrderByStartDesc(userId));
        }
    }

    @Override
    public List<Booking> getAllItemBookings(int userId, String state) {
        User user = userService.get(userId);
        switch (state) {
            case "CURRENT":
                return mapper.toBookingList(repository.findCurrentByOwner(
                        userId, LocalDateTime.now())
                );
            case "PAST":
                return mapper.toBookingList(repository.findPastByOwner(
                        userId, LocalDateTime.now()));
            case "FUTURE":
                return mapper.toBookingList(repository.findFutureByOwner(
                        userId, LocalDateTime.now()));
            case "WAITING":
                return mapper.toBookingList(repository.findByOwnerAndStatus(
                        userId, Status.WAITING));
            case "REJECTED":
                return mapper.toBookingList(repository.findByOwnerAndStatus(
                        userId, Status.REJECTED));
            default:
                return mapper.toBookingList(repository.findAllByOwner(userId));
        }
    }
}
