package com.esprit.ms.avis.service;

import com.esprit.ms.avis.Entities.Avis;
import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.external.ProductClient;
import com.esprit.ms.avis.mapper.AvisMapper;
import com.esprit.ms.avis.repository.AvisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service @RequiredArgsConstructor
public class AvisService {
    private final AvisRepository repo;
    private final ProductClient productClient;

    @Transactional
    public Avis create(AvisRequest req) {
        if (req.rating() < 1 || req.rating() > 5)
            throw new IllegalArgumentException("rating must be in [1..5]");

        // Feign check
        try {
            Boolean exists = productClient.productExists(req.productId());
            if (exists == null || !exists)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
        } catch (feign.FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not found");
        } catch (feign.FeignException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Product service unavailable");
        }

        var entity = AvisMapper.toEntity(req);
        return repo.save(entity);
    }

    public List<Avis> byProduct(String productId) { return repo.findByProductId(productId); }
    public List<Avis> byUser(Long userId) { return repo.findByUserId(userId); }
    public double averageForProduct(String productId) {
        var list = repo.findByProductId(productId);
        return list.isEmpty() ? 0.0 : list.stream().mapToInt(Avis::getRating).average().orElse(0.0);
    }
}
