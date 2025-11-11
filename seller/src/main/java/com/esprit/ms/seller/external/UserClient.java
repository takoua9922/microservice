package com.esprit.ms.seller.external;

import com.esprit.ms.seller.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userClient", url = "${user.service.url}")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserDto getById(@PathVariable("id") String id);
}