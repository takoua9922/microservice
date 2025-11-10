package com.ecommerce.cart.controller;

import com.ecommerce.cart.model.Cart;
import com.ecommerce.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    
    @GetMapping("/{sessionId}")
    public ResponseEntity<Cart> getCart(@PathVariable String sessionId) {
        return ResponseEntity.ok(cartService.getOrCreateCart(sessionId));
    }
    
    @PostMapping("/{sessionId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> request) {
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        return ResponseEntity.ok(cartService.addToCart(sessionId, productId, quantity));
    }
    
    @PutMapping("/{sessionId}/update")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> request) {
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        return ResponseEntity.ok(cartService.updateQuantity(sessionId, productId, quantity));
    }
    
    @DeleteMapping("/{sessionId}/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable String sessionId,
            @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeFromCart(sessionId, productId));
    }
    
    @DeleteMapping("/{sessionId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable String sessionId) {
        cartService.clearCart(sessionId);
        return ResponseEntity.ok().build();
    }
}
