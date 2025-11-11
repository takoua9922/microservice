// com.esprit.ms.avis.external.ProductClient
package com.esprit.ms.avis.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${ext.product.base-url:http://localhost:8082}")
public interface ProductClient {
    @GetMapping("/api/seller/products/{id}/exists")
    Boolean productExists(@PathVariable("id") String productId);
}
