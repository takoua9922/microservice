package com.esprit.ms.seller.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document("products")
@CompoundIndex(def = "{ title: 'text', description: 'text' }", name = "product_text_idx")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Product {
    @Id
    private String id;

    private String sellerId;
    private String title;
    private String description;
    private String category;

    private BigDecimal priceAmount;
    private String      priceCurrency;

    private Integer stock;
    private String  sku;

    private List<String> imageUrls;

    private ProductStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}