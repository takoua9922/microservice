package com.esprit.ms.avis.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${ext.user.base-url:}")
public interface UserClient {
    @GetMapping("/users/{id}/exists")
    Boolean userExists(@PathVariable("id") Long id);
}
