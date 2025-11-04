package com.esprit.ms.avis.dto;

import java.time.Instant;

public record AvisResponse(
        Long id, Long userId, Long articleId, int rating, String comment, Instant createdAt
) {}
