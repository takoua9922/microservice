package com.gestion.catalogue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example Product Controller demonstrating security annotations
 * Replace this with your actual product controller implementation
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Get all products
     * Public endpoint - no authentication required
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "List of all products (public access)");
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific product by ID
     * Public endpoint - no authentication required
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product details for ID: " + id);
        response.put("id", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new product
     * Requires authenticated user with 'admin' or 'manager' role
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'manager')")
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> product) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product created successfully");
        response.put("product", product);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing product
     * Requires authenticated user with 'admin' or 'manager' role
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'manager')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> product) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product updated successfully");
        response.put("id", id);
        response.put("product", product);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product
     * Requires authenticated user with 'admin' role only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        response.put("id", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get product inventory
     * Requires authenticated user
     */
    @GetMapping("/{id}/inventory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProductInventory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Inventory for product ID: " + id);
        response.put("id", id);
        response.put("stock", 100);
        return ResponseEntity.ok(response);
    }
}
