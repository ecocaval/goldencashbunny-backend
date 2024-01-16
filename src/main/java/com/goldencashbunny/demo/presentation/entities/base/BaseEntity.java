package com.goldencashbunny.demo.presentation.entities.base;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "timestamp", nullable = false)
    private LocalDateTime creationDate;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime lastModifiedDate;

    @Column(columnDefinition = "bool default FALSE", nullable = false)
    private boolean deleted;

}
