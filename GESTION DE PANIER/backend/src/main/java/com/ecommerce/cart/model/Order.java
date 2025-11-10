package com.ecommerce.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String customerName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String paymentMethod; // "CARTE" ou "ESPECE"
    private List<CartItem> items;
    private Double subtotal;
    private Double tvaAmount;
    private Double total;
    private String status; // "PENDING", "CONFIRMED", "DELIVERED"
    private LocalDateTime createdAt;
}
