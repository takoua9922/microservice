package com.esprit.ms.avis.repository;

import com.esprit.ms.avis.Entities.Avis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends MongoRepository<Avis, String> {
    List<Avis> findByProductId(String productId);
    List<Avis> findByUserId(Long userId);
}

