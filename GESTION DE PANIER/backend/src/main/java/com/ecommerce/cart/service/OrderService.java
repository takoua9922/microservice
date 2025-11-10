package com.ecommerce.cart.service;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.model.Order;
import com.ecommerce.cart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    
    public Order createOrder(String sessionId, Order orderDetails) {
        Cart cart = cartService.getOrCreateCart(sessionId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }
        
        Order order = new Order();
        order.setCustomerName(orderDetails.getCustomerName());
        order.setEmail(orderDetails.getEmail());
        order.setPhone(orderDetails.getPhone());
        order.setAddress(orderDetails.getAddress());
        order.setCity(orderDetails.getCity());
        order.setPostalCode(orderDetails.getPostalCode());
        order.setPaymentMethod(orderDetails.getPaymentMethod());
        order.setItems(cart.getItems());
        order.setSubtotal(cart.getSubtotal());
        order.setTvaAmount(cart.getTvaAmount());
        order.setTotal(cart.getTotal());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(sessionId);
        
        return savedOrder;
    }
    
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order updateOrderStatus(String orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
