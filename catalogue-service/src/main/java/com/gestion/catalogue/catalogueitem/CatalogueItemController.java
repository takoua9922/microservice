package com.gestion.catalogue.catalogueitem;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/catalogue/items")
@Validated
public class CatalogueItemController {

    private final CatalogueItemService catalogueItemService;

    public CatalogueItemController(CatalogueItemService catalogueItemService) {
        this.catalogueItemService = catalogueItemService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogueItem>> getAllItems() {
        return ResponseEntity.ok(catalogueItemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogueItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogueItemService.getItemById(id));
    }

    @PostMapping
    public ResponseEntity<CatalogueItem> createItem(@Valid @RequestBody CatalogueItem catalogueItem) {
        CatalogueItem createdItem = catalogueItemService.createItem(catalogueItem);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdItem.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogueItem> updateItem(@PathVariable Long id, @Valid @RequestBody CatalogueItem itemDetails) {
        CatalogueItem updatedItem = catalogueItemService.updateItem(id, itemDetails);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        catalogueItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
