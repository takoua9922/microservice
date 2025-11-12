package tn.esprit.ecommerce.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.ecommerce.Entities.Notification;
import tn.esprit.ecommerce.RestControllers.WebSocketController.NotificationMessage;

import java.time.LocalDateTime;

@Service
@Slf4j
public class WebSocketNotificationService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Envoie une notification via WebSocket à tous les clients connectés
     */
    public void sendNotificationToClients(Notification notification) {
        NotificationMessage message = new NotificationMessage();
        message.setType(notification.getType().name());
        message.setMessage(notification.getMessage());
        message.setSubject(notification.getSubject());
        message.setRecipientEmail(notification.getRecipientEmail());
        message.setTimestamp(LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/notifications", message);
        log.info("Notification envoyée via WebSocket: {}", message);
    }
    
    /**
     * Diffuse une promotion à tous les clients connectés
     */
    public void broadcastPromotion(String promotionName, Double discount) {
        NotificationMessage message = new NotificationMessage();
        message.setType("PROMOTION");
        message.setSubject("🎉 Nouvelle Promotion: " + promotionName);
        message.setMessage("Promotion active: " + promotionName + " - Réduction de " + discount + "%");
        message.setTimestamp(LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/promotions", message);
        log.info("Promotion diffusée via WebSocket: {}", message);
    }
    
    /**
     * Diffuse une notification de disponibilité de produit
     */
    public void broadcastProductAvailability(String productName) {
        NotificationMessage message = new NotificationMessage();
        message.setType("PRODUCT_AVAILABILITY");
        message.setSubject("✅ Produit Disponible: " + productName);
        message.setMessage("Le produit " + productName + " est maintenant disponible!");
        message.setTimestamp(LocalDateTime.now().toString());
        
        messagingTemplate.convertAndSend("/topic/products", message);
        log.info("Disponibilité produit diffusée via WebSocket: {}", message);
    }
}

