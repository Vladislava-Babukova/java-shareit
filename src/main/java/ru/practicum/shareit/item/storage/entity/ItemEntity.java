package ru.practicum.shareit.item.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.storage.entity.UserEntity;

import java.io.Serializable;

@Entity
@Table(schema = "public", name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "items_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, unique = true)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false, referencedColumnName = "id")
    private UserEntity owner;

    @Column(name = "request")
    private Integer request;
}