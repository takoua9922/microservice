// src/main/java/com/esprit/ms/avis/service/ReviewEventListener.java
package com.esprit.ms.avis.service;

import com.esprit.ms.avis.event.ReviewCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventListener {

    @RabbitListener(queues = "${app.mq.queue}")
    public void onReviewCreated(ReviewCreatedEvent event) {
        System.out.println("Received event: " + event.getId());
    }
}
