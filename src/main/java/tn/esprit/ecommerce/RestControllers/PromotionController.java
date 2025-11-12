package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ecommerce.Entities.Promotion;
import tn.esprit.ecommerce.Repositories.PromotionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
@Slf4j
@CrossOrigin(origins = "*")
public class PromotionController {
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    /**
     * Récupère toutes les promotions
     */
    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        return ResponseEntity.ok(promotionRepository.findAll());
    }
    
    /**
     * Récupère toutes les promotions actives
     */
    @GetMapping("/active")
    public ResponseEntity<List<Promotion>> getActivePromotions() {
        return ResponseEntity.ok(promotionRepository.findByIsActiveTrue());
    }
    
    /**
     * Récupère une promotion par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        return promotion.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crée une nouvelle promotion
     */
    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        try {
            Promotion savedPromotion = promotionRepository.save(promotion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPromotion);
        } catch (Exception e) {
            log.error("Erreur lors de la création de la promotion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Met à jour une promotion
     */
    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(
            @PathVariable Long id,
            @RequestBody Promotion promotion) {
        Optional<Promotion> existingPromotion = promotionRepository.findById(id);
        if (existingPromotion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        promotion.setId(id);
        Promotion updatedPromotion = promotionRepository.save(promotion);
        return ResponseEntity.ok(updatedPromotion);
    }
    
    /**
     * Supprime une promotion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        if (promotionRepository.existsById(id)) {
            promotionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

