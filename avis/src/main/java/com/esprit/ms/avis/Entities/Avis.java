package com.esprit.ms.avis.Entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "avis")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Avis {
    @Id private String id;
    @Indexed private String userId;
    @Indexed private String productId; // << not articleId
    private Integer rating;
    private String comment;
    @CreatedDate private Instant createdAt;
}

