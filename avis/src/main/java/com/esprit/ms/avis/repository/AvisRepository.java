package com.esprit.ms.avis.repository;
import com.esprit.ms.avis.Entities.Avis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvisRepository extends MongoRepository<Avis, Long> {
    List<Avis> findByArticleId(Long articleId);
    List<Avis> findByUserId(Long userId);
}