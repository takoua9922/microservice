// com.esprit.ms.avis.dto.AvisResponse
package com.esprit.ms.avis.dto;

import java.time.Instant;

public record AvisResponse(String id, String userId, String productId, int rating, String comment, Instant createdAt) {}
