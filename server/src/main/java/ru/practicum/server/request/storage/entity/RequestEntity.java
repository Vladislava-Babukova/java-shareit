package ru.practicum.server.request.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.user.storage.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(schema = "public", name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private int id;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "requestor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_requestor")
    )
    private UserEntity requestor;
    @Column(name = "created")
    private LocalDateTime created;
}
