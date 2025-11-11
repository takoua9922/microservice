package com.esprit.ms.seller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "user.client.enabled", havingValue = "true")
@EnableFeignClients(basePackages = "com.esprit.ms.seller.external")
class FeignConfig {}
