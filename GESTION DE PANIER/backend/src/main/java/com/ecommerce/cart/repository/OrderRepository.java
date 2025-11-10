package com.ecommerce.cart.repository;

import com.ecommerce.cart.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByCustomerName(String customerName);
    List<Order> findByEmail(String email);
}
