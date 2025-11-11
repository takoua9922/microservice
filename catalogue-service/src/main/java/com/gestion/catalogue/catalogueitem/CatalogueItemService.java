package com.gestion.catalogue.catalogueitem;

import com.gestion.catalogue.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatalogueItemService {

    private final CatalogueItemRepository catalogueItemRepository;

    public CatalogueItemService(CatalogueItemRepository catalogueItemRepository) {
        this.catalogueItemRepository = catalogueItemRepository;
    }

    public List<CatalogueItem> getAllItems() {
        return catalogueItemRepository.findAll();
    }

    public CatalogueItem getItemById(Long id) {
        return catalogueItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalogue item not found with id " + id));
    }

    @Transactional
    public CatalogueItem createItem(CatalogueItem catalogueItem) {
        catalogueItem.setId(null);
        return catalogueItemRepository.save(catalogueItem);
    }

    @Transactional
    public CatalogueItem updateItem(Long id, CatalogueItem itemDetails) {
        CatalogueItem existingItem = getItemById(id);

        existingItem.setName(itemDetails.getName());
        existingItem.setDescription(itemDetails.getDescription());
        existingItem.setPrice(itemDetails.getPrice());
        existingItem.setStock(itemDetails.getStock());
        existingItem.setCategory(itemDetails.getCategory());

        return catalogueItemRepository.save(existingItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        CatalogueItem existingItem = getItemById(id);
        catalogueItemRepository.delete(existingItem);
    }
}
