package ru.practicum.server.item.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.request.storage.entity.RequestEntity;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.io.Serializable;

@Entity
@Table(schema = "public", name = "items", catalog = "postgres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available", columnDefinition = "boolean default true")
    private Boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "id")
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id",
            foreignKey = @ForeignKey(name = "fk_request"))
    private RequestEntity request;
}