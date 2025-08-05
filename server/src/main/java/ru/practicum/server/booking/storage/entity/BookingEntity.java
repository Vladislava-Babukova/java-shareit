package ru.practicum.server.booking.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.item.storage.entity.ItemEntity;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "public", name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "bookings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private int id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item"))
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false, foreignKey = @ForeignKey(name = "fk_booker"))
    private UserEntity booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}