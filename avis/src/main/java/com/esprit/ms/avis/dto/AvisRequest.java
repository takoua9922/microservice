package com.esprit.ms.avis.dto;

import jakarta.validation.constraints.*;

public record AvisRequest(String userId, String productId, int rating, String comment) {}
