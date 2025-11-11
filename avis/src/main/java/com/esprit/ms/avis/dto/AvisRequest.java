package com.esprit.ms.avis.dto;


import jakarta.validation.constraints.*;

public record AvisRequest(
        @NotNull
        Long userId,
        @NotNull
        Long articleId,
        @Min(1)
        @Max(5)
        int rating,
        @Size(max=1000)
        String comment
) {}
