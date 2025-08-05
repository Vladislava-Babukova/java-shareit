package ru.practicum.server.booking.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.storage.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    List<BookingEntity> findByBookerIdOrderByStartDesc(int bookerId);

    List<BookingEntity> findByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime time);

    List<BookingEntity> findByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime time);

    List<BookingEntity> findByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status);

    List<BookingEntity> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId, LocalDateTime time,
                                                                                LocalDateTime time2);


    @Query("SELECT b FROM BookingEntity b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<BookingEntity> findAllByOwner(@Param("ownerId") int ownerId);

    @Query("SELECT b FROM BookingEntity b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.end < :currentTime " +
            "ORDER BY b.start DESC")
    List<BookingEntity> findPastByOwner(
            @Param("ownerId") int ownerId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM BookingEntity b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<BookingEntity> findFutureByOwner(
            @Param("ownerId") int ownerId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM BookingEntity b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<BookingEntity> findByOwnerAndStatus(
            @Param("ownerId") int ownerId,
            @Param("status") Status status);

    @Query("SELECT b FROM BookingEntity b " +
            "JOIN FETCH b.item i " +
            "WHERE i.owner.id = :ownerId " +
            "AND b.start <= :currentTime " +
            "AND b.end >= :currentTime " +
            "ORDER BY b.start DESC")
    List<BookingEntity> findCurrentByOwner(
            @Param("ownerId") int ownerId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM BookingEntity b " +
            "WHERE b.item.id = :itemId " +
            "AND b.end < :currentTime " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.end DESC " +
            "LIMIT 1")
    List<BookingEntity> findLastBookingDate(
            @Param("itemId") int itemId,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM BookingEntity b " +
            "WHERE b.item.id = :itemId " +
            "AND b.start > :currentTime " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start ASC " +
            "LIMIT 1")
    List<BookingEntity> findNextBookingDate(
            @Param("itemId") int itemId,
            @Param("currentTime") LocalDateTime currentTime);


    List<BookingEntity> findByItemIdAndBookerIdAndEndBefore(int itemId, int userId, LocalDateTime time);
}

