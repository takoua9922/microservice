package tn.esprit.ecommerce.Repositories;

import tn.esprit.ecommerce.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByIsAvailableTrue();
    
    List<Product> findByIsAvailableFalse();
    
    Optional<Product> findByIdAndIsAvailableTrue(Long id);
    
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
}

