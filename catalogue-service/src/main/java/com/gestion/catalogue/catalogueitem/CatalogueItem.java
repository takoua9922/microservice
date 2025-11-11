package com.gestion.catalogue.catalogueitem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "catalogue_items")
public class CatalogueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Item name is required")
    private String name;

    @Column(length = 2048)
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Item price is required")
    @PositiveOrZero(message = "Item price must be non-negative")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "Item stock is required")
    @PositiveOrZero(message = "Item stock must be non-negative")
    private Integer stock = 0;

    @Column
    private String category;

    @PrePersist
    void ensureDefaults() {
        if (stock == null) {
            stock = 0;
        }
    }

    public CatalogueItem() {
    }

    public CatalogueItem(Long id, String name, String description, BigDecimal price, Integer stock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
