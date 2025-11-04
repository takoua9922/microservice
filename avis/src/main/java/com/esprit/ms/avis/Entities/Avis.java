package com.esprit.ms.avis.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name="avis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long articleId;

    @Column(nullable = false)
    private Integer rating; // 1..5

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
}

