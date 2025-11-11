package com.esprit.ms.seller.Controller;

import com.esprit.ms.seller.Entities.Product;
import com.esprit.ms.seller.dto.ProductCreateDto;
import com.esprit.ms.seller.dto.ProductUpdateDto;
import com.esprit.ms.seller.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping("/{sellerId}")
    public Product create(@PathVariable String sellerId,
                          @Valid @RequestBody ProductCreateDto dto) {
        return service.create(sellerId, dto);
    }

    @GetMapping("/{sellerId}")
    public Page<Product> listMine(@PathVariable String sellerId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        String[] parts = sort.split(",");
        Sort s = (parts.length == 2 && "desc".equalsIgnoreCase(parts[1]))
                ? Sort.by(Sort.Order.desc(parts[0]))
                : Sort.by(Sort.Order.asc(parts[0]));
        Pageable pageable = PageRequest.of(page, size, s);
        return service.listMine(sellerId, pageable);
    }

    @GetMapping("/{sellerId}/{id}")
    public Product getMine(@PathVariable String sellerId, @PathVariable String id) {
        return service.getMine(sellerId, id);
    }

    @PutMapping("/{sellerId}/{id}")
    public Product updateMine(@PathVariable String sellerId, @PathVariable String id,
                              @Valid @RequestBody ProductUpdateDto dto) {
        return service.updateMine(sellerId, id, dto);
    }

    @DeleteMapping("/{sellerId}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMine(@PathVariable String sellerId, @PathVariable String id) {
        service.deleteMine(sellerId, id);
    }
}