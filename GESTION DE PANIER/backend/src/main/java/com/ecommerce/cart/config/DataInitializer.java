package com.ecommerce.cart.config;

import com.ecommerce.cart.model.Product;
import com.ecommerce.cart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                new Product(null, "Laptop Dell XPS 13", "Ordinateur portable haute performance", 1299.99, 
                    "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=400", 10, "Électronique"),
                new Product(null, "iPhone 15 Pro", "Smartphone Apple dernière génération", 1199.99, 
                    "https://images.unsplash.com/photo-1592286927505-4fd4d3d4ef9f?w=400", 15, "Électronique"),
                new Product(null, "Samsung Galaxy S24", "Smartphone Samsung flagship", 999.99, 
                    "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400", 20, "Électronique"),
                new Product(null, "AirPods Pro", "Écouteurs sans fil avec réduction de bruit", 249.99, 
                    "https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=400", 30, "Accessoires"),
                new Product(null, "Sony WH-1000XM5", "Casque audio premium", 399.99, 
                    "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=400", 12, "Accessoires"),
                new Product(null, "iPad Air", "Tablette Apple polyvalente", 649.99, 
                    "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400", 18, "Électronique"),
                new Product(null, "Apple Watch Series 9", "Montre connectée intelligente", 449.99, 
                    "https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=400", 25, "Accessoires"),
                new Product(null, "MacBook Pro 14\"", "Ordinateur portable professionnel", 2199.99, 
                    "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", 8, "Électronique"),
                new Product(null, "Logitech MX Master 3", "Souris ergonomique sans fil", 99.99, 
                    "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400", 40, "Accessoires"),
                new Product(null, "Samsung 4K Monitor", "Écran 27 pouces Ultra HD", 449.99, 
                    "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400", 15, "Électronique")
            );
            
            productRepository.saveAll(products);
            System.out.println("✅ Base de données initialisée avec " + products.size() + " produits");
        }
    }
}
