package com.esprit.ms.avis.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// TODO: remplace "article-service" et les paths quand le service existera
@FeignClient(name = "article-service", url = "${ext.article.base-url:}")
public interface ArticleClient {
    @GetMapping("/articles/{id}/exists")
    Boolean articleExists(@PathVariable("id") Long id);
}
