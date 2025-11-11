package com.esprit.ms.seller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateDto {
    private String title;
    private String description;
    private String category;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private Integer stock;
    private String sku;
    private List<String> imageUrls;
}