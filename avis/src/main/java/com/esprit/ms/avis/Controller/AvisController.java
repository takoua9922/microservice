package com.esprit.ms.avis.Controller;



import com.esprit.ms.avis.service.AvisService;
import com.esprit.ms.avis.dto.AvisRequest;
import com.esprit.ms.avis.dto.AvisResponse;
import com.esprit.ms.avis.mapper.AvisMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> listByArticle(@PathVariable Long articleId) {
        var list = service.byArticle(articleId).stream().map(AvisMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId) {
        var list = service.byUser(userId).stream().map(AvisMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/article/{articleId}/average")
    public ResponseEntity<Double> average(@PathVariable Long articleId) {
        return ResponseEntity.ok(service.averageForArticle(articleId));
    }
}
