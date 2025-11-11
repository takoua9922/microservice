package com.esprit.ms.avis.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${ext.product.base-url:http://localhost:9080}")
public interface UserClient {
    @GetMapping("/api/v1/customers/exists/{customer-id}")
    Boolean userExists(@PathVariable("customer-id") String id);
}