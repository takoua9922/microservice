package tn.esprit.ecommerce.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduledNotificationService {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Vérifie et envoie des notifications pour les promotions actives
     * Exécuté toutes les heures
     */
    @Scheduled(fixedRate = 3600000) // 1 heure en millisecondes
    public void checkActivePromotions() {
        log.info("Vérification automatique des promotions actives...");
        try {
            notificationService.checkAndSendActivePromotionNotifications();
        } catch (Exception e) {
            log.error("Erreur lors de la vérification des promotions: {}", e.getMessage());
        }
    }
    
    /**
     * Vérifie la disponibilité des produits et envoie des notifications
     * Exécuté toutes les 30 minutes
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes en millisecondes
    public void checkProductAvailability() {
        log.info("Vérification automatique de la disponibilité des produits...");
        try {
            notificationService.checkProductAvailabilityAndNotify();
        } catch (Exception e) {
            log.error("Erreur lors de la vérification de disponibilité: {}", e.getMessage());
        }
    }
}

