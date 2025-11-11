package com.esprit.ms.avis.mapper;

import com.esprit.ms.avis.Entities.Avis;
import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.dto.AvisResponse;

public final class AvisMapper {
    public static Avis toEntity(AvisRequest r) {
        return Avis.builder()
                .userId(r.userId())
                .productId(r.productId())
                .rating(r.rating())
                .comment(r.comment())
                .build();
    }
    public static AvisResponse toDto(Avis a) {
        return new AvisResponse(
                a.getId(), a.getUserId(), a.getProductId(), a.getRating(), a.getComment(), a.getCreatedAt()
        );
    }
}
