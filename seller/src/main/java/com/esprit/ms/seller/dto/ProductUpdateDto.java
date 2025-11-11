package com.esprit.ms.seller.dto;

import com.esprit.ms.seller.Entities.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class ProductUpdateDto {
    private String title;
    private String description;
    private String category;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private Integer stock;
    private String sku;
    private List<String> imageUrls;
    private ProductStatus status;
}