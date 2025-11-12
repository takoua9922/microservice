package tn.esprit.ecommerce.Repositories;

import tn.esprit.ecommerce.Entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    
    List<Promotion> findByIsActiveTrue();
    
    @Query("SELECT p FROM Promotion p WHERE p.isActive = true AND p.startDate <= :now AND p.endDate >= :now")
    List<Promotion> findActivePromotions(LocalDateTime now);
    
    List<Promotion> findByProductId(Long productId);
}

