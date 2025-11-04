package com.esprit.ms.avis.repository;
import com.esprit.ms.avis.Entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByArticleId(Long articleId);
    List<Avis> findByUserId(Long userId);
}
