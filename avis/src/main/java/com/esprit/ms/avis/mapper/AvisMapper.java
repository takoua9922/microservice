package com.esprit.ms.avis.mapper;


import com.esprit.ms.avis.Entities.Avis;
import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.dto.AvisResponse;

public class AvisMapper {
    public static Avis toEntity(AvisRequest req) {
        return Avis.builder()
                .userId(req.userId())
                .articleId(req.articleId())
                .rating(req.rating())
                .comment(req.comment())
                .build();
    }
    public static AvisResponse toDto(Avis a) {
        return new AvisResponse(a.getId(), a.getUserId(), a.getArticleId(),
                a.getRating(), a.getComment(), a.getCreatedAt());
    }
}
