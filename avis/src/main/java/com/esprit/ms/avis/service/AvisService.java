package com.esprit.ms.avis.service;

import com.esprit.ms.avis.Entities.Avis;
import com.esprit.ms.avis.repository.AvisRepository;
import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.mapper.AvisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class AvisService {
    private final AvisRepository repo;

    @Transactional
    public Avis create(AvisRequest req) {

        Avis entity = AvisMapper.toEntity(req);
        return repo.save(entity);
    }

    public List<Avis> byArticle(Long articleId) { return repo.findByArticleId(articleId); }
    public List<Avis> byUser(Long userId) { return repo.findByUserId(userId); }
    public double averageForArticle(Long articleId) {
        var list = repo.findByArticleId(articleId);
        return list.isEmpty() ? 0.0 : list.stream().mapToInt(Avis::getRating).average().orElse(0.0);
    }
}
