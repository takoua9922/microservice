package com.gestion.catalogue.catalogueitem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gestion.catalogue.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CatalogueItemServiceTests {

    @Mock
    private CatalogueItemRepository catalogueItemRepository;

    @InjectMocks
    private CatalogueItemService catalogueItemService;

    @Test
    @DisplayName("createItem assigns null id and persists entity")
    void createItem_shouldPersistWithGeneratedId() {
        CatalogueItem item = new CatalogueItem(null, "Laptop", "High-end", BigDecimal.TEN, 5, "Electronics");
        when(catalogueItemRepository.save(any(CatalogueItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CatalogueItem saved = catalogueItemService.createItem(item);

        assertThat(saved.getId()).isNull();
        assertThat(saved.getName()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("getItemById throws when not found")
    void getItemById_notFound_shouldThrow() {
        when(catalogueItemRepository.findById(99L)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> catalogueItemService.getItemById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Catalogue item not found with id 99");
    }
}
