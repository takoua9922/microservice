package com.esprit.ms.seller.repository;

import com.esprit.ms.seller.Entities.Product;
import com.esprit.ms.seller.Entities.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findBySellerId(String sellerId, Pageable pageable);
    boolean existsBySellerIdAndSku(String sellerId, String sku);

}
