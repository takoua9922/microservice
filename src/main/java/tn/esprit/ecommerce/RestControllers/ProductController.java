package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ecommerce.Entities.Product;
import tn.esprit.ecommerce.Repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Récupère tous les produits
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
    
    /**
     * Récupère tous les produits disponibles
     */
    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        return ResponseEntity.ok(productRepository.findByIsAvailableTrue());
    }
    
    /**
     * Récupère un produit par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crée un nouveau produit
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            log.error("Erreur lors de la création du produit: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Met à jour un produit
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Met à jour la disponibilité d'un produit
     */
    @PatchMapping("/{id}/availability")
    public ResponseEntity<Product> updateProductAvailability(
            @PathVariable Long id,
            @RequestParam Boolean isAvailable,
            @RequestParam(required = false) Integer stockQuantity) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Product product = productOpt.get();
        product.setIsAvailable(isAvailable);
        if (stockQuantity != null) {
            product.setStockQuantity(stockQuantity);
        }
        
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * Supprime un produit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

