package com.ecommerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String sessionId;
    private List<CartItem> items = new ArrayList<>();
    private Double tvaRate = 0.20; // TVA 20% par défaut
    
    public Double getSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }
    
    public Double getTvaAmount() {
        return getSubtotal() * tvaRate;
    }
    
    public Double getTotal() {
        return getSubtotal() + getTvaAmount();
    }
}
