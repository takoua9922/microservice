// com.esprit.ms.avis.Controller.AvisController
package com.esprit.ms.avis.Controller;

import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.dto.AvisResponse;
import com.esprit.ms.avis.mapper.AvisMapper;
import com.esprit.ms.avis.service.AvisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avis")
@RequiredArgsConstructor

public class AvisController {
    private final AvisService service;

    @PostMapping
    public ResponseEntity<AvisResponse> create(@Valid @RequestBody AvisRequest req) {
        var saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/avis/" + saved.getId()))
                .body(AvisMapper.toDto(saved));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<AvisResponse>> listByProduct(@PathVariable String productId) {
        var list = service.byProduct(productId).stream().map(AvisMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/product/{productId}/average")
    public ResponseEntity<Double> average(@PathVariable String productId) {
        return ResponseEntity.ok(service.averageForProduct(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AvisResponse>> listByUser(@PathVariable Long userId) {
        var list = service.byUser(userId).stream().map(AvisMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }
}