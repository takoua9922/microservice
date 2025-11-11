package com.esprit.ms.avis.dto;


public record AvisRequest(String userId, String productId, int rating, String comment) {}
