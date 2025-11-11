package com.esprit.ms.seller.service;

import com.esprit.ms.seller.Entities.Product;
import com.esprit.ms.seller.Entities.ProductStatus;

import com.esprit.ms.seller.dto.ProductCreateDto;
import com.esprit.ms.seller.dto.ProductUpdateDto;
import com.esprit.ms.seller.dto.UserDto;
import com.esprit.ms.seller.external.UserClient;
import com.esprit.ms.seller.repository.ProductRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final UserClient userClient;

    public Product create(String sellerId, ProductCreateDto dto) {
        // TEMP: skip user check since MS not available
        // assertSellerActive(sellerId);

        if (repo.existsBySellerIdAndSku(sellerId, dto.getSku())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU already exists for this seller");
        }

        Product p = Product.builder()
                .sellerId(sellerId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .priceAmount(dto.getPriceAmount())
                .priceCurrency(dto.getPriceCurrency())
                .stock(dto.getStock())
                .sku(dto.getSku())
                .imageUrls(Optional.ofNullable(dto.getImageUrls()).orElse(List.of()))
                .status(ProductStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return repo.save(p);
    }

    public Page<Product> listMine(String sellerId, Pageable pageable) {
        // TEMP: skip user check since MS not available
        // assertSellerActive(sellerId);
        return repo.findBySellerId(sellerId, pageable);
    }

    public Product getMine(String sellerId, String id) {
        Product p = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!p.getSellerId().equals(sellerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return p;
    }

    public Product updateMine(String sellerId, String id, ProductUpdateDto dto) {
        Product p = getMine(sellerId, id);

        // TEMP: skip user check since MS not available
        // assertSellerActive(sellerId);

        if (dto.getTitle() != null) p.setTitle(dto.getTitle());
        if (dto.getDescription() != null) p.setDescription(dto.getDescription());
        if (dto.getCategory() != null) p.setCategory(dto.getCategory());
        if (dto.getPriceAmount() != null) p.setPriceAmount(dto.getPriceAmount());
        if (dto.getPriceCurrency() != null) p.setPriceCurrency(dto.getPriceCurrency());
        if (dto.getStock() != null) p.setStock(dto.getStock());

        if (dto.getSku() != null && !dto.getSku().equals(p.getSku())) {
            if (repo.existsBySellerIdAndSku(sellerId, dto.getSku())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU already exists for this seller");
            }
            p.setSku(dto.getSku());
        }

        if (dto.getImageUrls() != null) p.setImageUrls(dto.getImageUrls());
        if (dto.getStatus() != null) p.setStatus(dto.getStatus());

        p.setUpdatedAt(Instant.now());

        return repo.save(p);
    }

    public void deleteMine(String sellerId, String id) {
        Product p = getMine(sellerId, id);
        repo.delete(p);
    }

    /* ===================== Helpers ===================== */

    private void assertSellerActive(String sellerId) {
        if (userClient == null) return; // skip while user MS not integrated
        UserDto user;
        try {
            user = userClient.getById(sellerId);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "User service unavailable");
        }
        if (!"SELLER".equalsIgnoreCase(user.role()))  throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a seller");
        if (!"ACTIVE".equalsIgnoreCase(user.status())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seller is not active");
    }
    public boolean existsById(String id) {
        return repo.existsById(id);
    }
}