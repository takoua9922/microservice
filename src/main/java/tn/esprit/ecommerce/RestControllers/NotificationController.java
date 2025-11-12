package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ecommerce.Entities.Notification;
import tn.esprit.ecommerce.Services.NotificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Envoie une notification personnalisée (avec validation d'authentification)
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> request) {
        try {
            // Extraire le token du header Authorization (gère "Bearer token" ou juste "token")
            String token = extractTokenFromHeader(authHeader);
            
            // Si pas de token dans le header, essayer dans le body
            if (token == null || token.isEmpty()) {
                token = request.get("token");
            }
            
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token d'authentification requis. " +
                        "Utilisez le header 'Authorization: Bearer token' ou ajoutez 'token' dans le body"));
            }
            
            String recipientEmail = request.get("recipientEmail");
            String subject = request.get("subject");
            String message = request.get("message");
            
            if (recipientEmail == null || subject == null || message == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "recipientEmail, subject et message sont requis"));
            }
            
            Notification notification = notificationService.sendCustomNotification(
                token, recipientEmail, subject, message
            );
            
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de l'envoi de la notification"));
        }
    }
    
    /**
     * Extrait le token du header Authorization
     * Gère les formats : "Bearer token", "token", ou null
     */
    private String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }
        
        // Si le header commence par "Bearer ", extraire le token
        if (authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer ")) {
            return authHeader.substring(7).trim();
        }
        
        // Sinon, retourner le header tel quel (cas où c'est juste le token)
        return authHeader.trim();
    }
    
    /**
     * Déclenche l'envoi de notifications pour une promotion
     */
    @PostMapping("/promotions/{promotionId}/notify")
    public ResponseEntity<?> notifyPromotion(@PathVariable Long promotionId) {
        try {
            notificationService.sendPromotionNotifications(promotionId);
            return ResponseEntity.ok(Map.of(
                "message", "Notifications de promotion envoyées avec succès",
                "promotionId", promotionId
            ));
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notifications de promotion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de l'envoi des notifications"));
        }
    }
    
    /**
     * Envoie une notification de disponibilité de produit
     */
    @PostMapping("/products/{productId}/availability")
    public ResponseEntity<?> notifyProductAvailability(
            @PathVariable Long productId,
            @RequestParam String email) {
        try {
            notificationService.sendProductAvailabilityNotification(productId, email);
            return ResponseEntity.ok(Map.of(
                "message", "Notification de disponibilité envoyée avec succès",
                "productId", productId,
                "email", email
            ));
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification de disponibilité: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de l'envoi de la notification"));
        }
    }
    
    /**
     * Envoie une notification de rupture de stock
     */
    @PostMapping("/products/{productId}/out-of-stock")
    public ResponseEntity<?> notifyProductOutOfStock(
            @PathVariable Long productId,
            @RequestParam String email) {
        try {
            notificationService.sendProductOutOfStockNotification(productId, email);
            return ResponseEntity.ok(Map.of(
                "message", "Notification de rupture de stock envoyée avec succès",
                "productId", productId,
                "email", email
            ));
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification de rupture: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de l'envoi de la notification"));
        }
    }
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Récupère toutes les notifications par email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Notification>> getNotificationsByEmail(@PathVariable String email) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByEmail(email);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Déclenche la vérification et l'envoi de notifications pour les promotions actives
     */
    @PostMapping("/promotions/check-active")
    public ResponseEntity<?> checkActivePromotions() {
        try {
            notificationService.checkAndSendActivePromotionNotifications();
            return ResponseEntity.ok(Map.of(
                "message", "Vérification des promotions actives lancée"
            ));
        } catch (Exception e) {
            log.error("Erreur lors de la vérification des promotions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la vérification"));
        }
    }
    
    /**
     * Déclenche la vérification de disponibilité des produits
     */
    @PostMapping("/products/check-availability")
    public ResponseEntity<?> checkProductAvailability() {
        try {
            notificationService.checkProductAvailabilityAndNotify();
            return ResponseEntity.ok(Map.of(
                "message", "Vérification de disponibilité des produits lancée"
            ));
        } catch (Exception e) {
            log.error("Erreur lors de la vérification de disponibilité: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur lors de la vérification"));
        }
    }
}

