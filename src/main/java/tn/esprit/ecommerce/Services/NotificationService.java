package tn.esprit.ecommerce.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ecommerce.Entities.*;
import tn.esprit.ecommerce.Repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    /**
     * Envoie une notification de promotion à tous les utilisateurs intéressés
     */
    @Async
    @Transactional
    public void sendPromotionNotifications(Long promotionId) {
        Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
        if (promotionOpt.isEmpty() || !promotionOpt.get().getIsActive()) {
            log.warn("Promotion {} non trouvée ou inactive", promotionId);
            return;
        }
        
        Promotion promotion = promotionOpt.get();
        List<UserNotification> users = userNotificationRepository.findByWantsPromoNotificationsTrue();
        
        log.info("Envoi de notifications de promotion à {} utilisateurs", users.size());
        
        for (UserNotification user : users) {
            try {
                emailService.sendPromotionNotification(
                    user.getEmail(),
                    promotion.getName(),
                    promotion.getDiscountPercentage()
                );
                
                Notification notification = new Notification();
                notification.setRecipientEmail(user.getEmail());
                notification.setSubject("🎉 Nouvelle Promotion: " + promotion.getName());
                notification.setMessage("Promotion: " + promotion.getName() + " - Réduction de " + 
                    promotion.getDiscountPercentage() + "%");
                notification.setType(Notification.NotificationType.PROMOTION);
                notification.setSentAt(LocalDateTime.now());
                notification.setIsSent(true);
                notification.setUserId(user.getUserId());
                notification.setPromotionId(promotionId);
                
                notificationRepository.save(notification);
                log.info("Notification de promotion envoyée à: {}", user.getEmail());
                
                // Envoyer aussi via WebSocket
                webSocketNotificationService.sendNotificationToClients(notification);
            } catch (Exception e) {
                log.error("Erreur lors de l'envoi de notification à {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }
    
    /**
     * Envoie une notification de disponibilité de produit
     */
    @Async
    @Transactional
    public void sendProductAvailabilityNotification(Long productId, String userEmail) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Produit {} non trouvé", productId);
            return;
        }
        
        Product product = productOpt.get();
        if (!product.getIsAvailable() || product.getStockQuantity() <= 0) {
            log.warn("Produit {} non disponible", productId);
            return;
        }
        
        Optional<UserNotification> userOpt = userNotificationRepository.findByEmail(userEmail);
        if (userOpt.isEmpty() || !userOpt.get().getWantsAvailabilityNotifications()) {
            log.info("Utilisateur {} ne souhaite pas recevoir de notifications de disponibilité", userEmail);
            return;
        }
        
        try {
            emailService.sendProductAvailabilityNotification(userEmail, product.getName());
            
            Notification notification = new Notification();
            notification.setRecipientEmail(userEmail);
            notification.setSubject("✅ Produit Disponible: " + product.getName());
            notification.setMessage("Le produit " + product.getName() + " est maintenant disponible!");
            notification.setType(Notification.NotificationType.PRODUCT_AVAILABILITY);
            notification.setSentAt(LocalDateTime.now());
            notification.setIsSent(true);
            notification.setProductId(productId);
            notification.setUserId(userOpt.get().getUserId());
            
            notificationRepository.save(notification);
            log.info("Notification de disponibilité envoyée à: {}", userEmail);
            
            // Envoyer aussi via WebSocket
            webSocketNotificationService.sendNotificationToClients(notification);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification à {}: {}", userEmail, e.getMessage());
        }
    }
    
    /**
     * Vérifie et envoie des notifications pour les promotions actives
     */
    @Async
    public void checkAndSendActivePromotionNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> activePromotions = promotionRepository.findActivePromotions(now);
        
        log.info("Vérification de {} promotions actives", activePromotions.size());
        
        for (Promotion promotion : activePromotions) {
            // Vérifier si des notifications ont déjà été envoyées récemment (dans les dernières 24h)
            List<Notification> recentNotifications = notificationRepository
                .findByType(Notification.NotificationType.PROMOTION)
                .stream()
                .filter(n -> n.getPromotionId() != null && n.getPromotionId().equals(promotion.getId()))
                .filter(n -> n.getSentAt().isAfter(now.minusHours(24)))
                .toList();
            
            if (recentNotifications.isEmpty()) {
                sendPromotionNotifications(promotion.getId());
            }
        }
    }
    
    /**
     * Vérifie les produits qui redeviennent disponibles et envoie des notifications
     */
    @Async
    public void checkProductAvailabilityAndNotify() {
        List<Product> allProducts = productRepository.findAll();
        List<UserNotification> interestedUsers = userNotificationRepository
            .findByWantsAvailabilityNotificationsTrue();
        
        for (Product product : allProducts) {
            // Vérifier si le produit est disponible maintenant
            boolean isNowAvailable = product.getIsAvailable() && product.getStockQuantity() > 0;
            
            // Vérifier si une notification a déjà été envoyée récemment pour ce produit
            List<Notification> recentNotifications = notificationRepository
                .findByType(Notification.NotificationType.PRODUCT_AVAILABILITY)
                .stream()
                .filter(n -> n.getProductId() != null && n.getProductId().equals(product.getId()))
                .filter(n -> n.getSentAt().isAfter(LocalDateTime.now().minusHours(24)))
                .toList();
            
            if (isNowAvailable && recentNotifications.isEmpty()) {
                // Produit disponible - envoyer notification à tous les utilisateurs intéressés
                for (UserNotification user : interestedUsers) {
                    sendProductAvailabilityNotification(product.getId(), user.getEmail());
                }
            } else if (!isNowAvailable && product.getStockQuantity() == 0) {
                // Produit en rupture de stock - envoyer notification de rupture
                for (UserNotification user : interestedUsers) {
                    sendProductOutOfStockNotification(product.getId(), user.getEmail());
                }
            }
        }
    }
    
    /**
     * Envoie une notification de rupture de stock
     */
    @Async
    @Transactional
    public void sendProductOutOfStockNotification(Long productId, String userEmail) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Produit {} non trouvé", productId);
            return;
        }
        
        Product product = productOpt.get();
        Optional<UserNotification> userOpt = userNotificationRepository.findByEmail(userEmail);
        if (userOpt.isEmpty() || !userOpt.get().getWantsAvailabilityNotifications()) {
            return;
        }
        
        try {
            emailService.sendProductOutOfStockNotification(userEmail, product.getName());
            
            Notification notification = new Notification();
            notification.setRecipientEmail(userEmail);
            notification.setSubject("⚠️ Produit en Rupture de Stock: " + product.getName());
            notification.setMessage("Le produit " + product.getName() + " est en rupture de stock.");
            notification.setType(Notification.NotificationType.PRODUCT_AVAILABILITY);
            notification.setSentAt(LocalDateTime.now());
            notification.setIsSent(true);
            notification.setProductId(productId);
            notification.setUserId(userOpt.get().getUserId());
            
            notificationRepository.save(notification);
            log.info("Notification de rupture de stock envoyée à: {}", userEmail);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification de rupture à {}: {}", userEmail, e.getMessage());
        }
    }
    
    /**
     * Envoie une notification personnalisée avec validation d'authentification
     */
    @Transactional
    public Notification sendCustomNotification(String token, String recipientEmail, 
                                                String subject, String message) {
        // Valider le token
        Long userId = authService.validateToken(token);
        if (userId == null) {
            throw new RuntimeException("Token d'authentification invalide");
        }
        
        try {
            emailService.sendSimpleEmail(recipientEmail, subject, message);
            
            Notification notification = new Notification();
            notification.setRecipientEmail(recipientEmail);
            notification.setSubject(subject);
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.GENERAL);
            notification.setSentAt(LocalDateTime.now());
            notification.setIsSent(true);
            notification.setUserId(userId);
            
            Notification savedNotification = notificationRepository.save(notification);
            
            // Envoyer aussi via WebSocket
            webSocketNotificationService.sendNotificationToClients(savedNotification);
            
            return savedNotification;
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de notification personnalisée: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de la notification", e);
        }
    }
    
    /**
     * Récupère toutes les notifications d'un utilisateur
     */
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
    
    /**
     * Récupère toutes les notifications par email
     */
    public List<Notification> getNotificationsByEmail(String email) {
        return notificationRepository.findByRecipientEmail(email);
    }
}

