package com.ecommerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    
    public Double getSubtotal() {
        return price * quantity;
    }
}
