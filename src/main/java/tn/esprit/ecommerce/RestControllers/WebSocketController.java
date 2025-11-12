package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import tn.esprit.ecommerce.Entities.Notification;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class WebSocketController {

    /**
     * Reçoit un message via WebSocket et le diffuse à tous les clients connectés
     */
    @MessageMapping("/notifications/send")
    @SendTo("/topic/notifications")
    public NotificationMessage handleNotification(NotificationMessage message) {
        log.info("Message WebSocket reçu: {}", message);
        
        NotificationMessage response = new NotificationMessage();
        response.setType("NOTIFICATION");
        response.setMessage(message.getMessage());
        response.setRecipientEmail(message.getRecipientEmail());
        response.setSubject(message.getSubject());
        response.setTimestamp(LocalDateTime.now().toString());
        
        return response;
    }

    /**
     * Envoie une notification de promotion à tous les clients connectés
     */
    @MessageMapping("/promotions/broadcast")
    @SendTo("/topic/promotions")
    public NotificationMessage broadcastPromotion(NotificationMessage message) {
        log.info("Promotion diffusée via WebSocket: {}", message);
        
        NotificationMessage response = new NotificationMessage();
        response.setType("PROMOTION");
        response.setMessage(message.getMessage());
        response.setSubject(message.getSubject());
        response.setTimestamp(LocalDateTime.now().toString());
        
        return response;
    }

    /**
     * Envoie une notification de disponibilité de produit
     */
    @MessageMapping("/products/availability")
    @SendTo("/topic/products")
    public NotificationMessage broadcastProductAvailability(NotificationMessage message) {
        log.info("Disponibilité produit diffusée via WebSocket: {}", message);
        
        NotificationMessage response = new NotificationMessage();
        response.setType("PRODUCT_AVAILABILITY");
        response.setMessage(message.getMessage());
        response.setSubject(message.getSubject());
        response.setTimestamp(LocalDateTime.now().toString());
        
        return response;
    }

    /**
     * Classe pour les messages WebSocket
     */
    public static class NotificationMessage {
        private String type;
        private String message;
        private String subject;
        private String recipientEmail;
        private String timestamp;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getRecipientEmail() {
            return recipientEmail;
        }

        public void setRecipientEmail(String recipientEmail) {
            this.recipientEmail = recipientEmail;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "NotificationMessage{" +
                    "type='" + type + '\'' +
                    ", message='" + message + '\'' +
                    ", subject='" + subject + '\'' +
                    ", recipientEmail='" + recipientEmail + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}

