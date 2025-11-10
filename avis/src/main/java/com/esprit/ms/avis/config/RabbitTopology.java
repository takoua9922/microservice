package com.esprit.ms.avis.config;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopology {
    @Value("${app.mq.exchange}")
    String exchange;

    @Bean
    TopicExchange avisExchange() {
        return ExchangeBuilder.topicExchange(exchange)
                .durable(true)
                .build();
    }
}