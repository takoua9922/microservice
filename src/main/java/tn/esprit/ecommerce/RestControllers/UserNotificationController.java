package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ecommerce.Entities.UserNotification;
import tn.esprit.ecommerce.Repositories.UserNotificationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-notifications")
@Slf4j
@CrossOrigin(origins = "*")
public class UserNotificationController {
    
    @Autowired
    private UserNotificationRepository userNotificationRepository;
    
    /**
     * Récupère toutes les préférences de notifications
     */
    @GetMapping
    public ResponseEntity<List<UserNotification>> getAllUserNotifications() {
        return ResponseEntity.ok(userNotificationRepository.findAll());
    }
    
    /**
     * Récupère les préférences d'un utilisateur
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserNotification> getUserNotificationByUserId(@PathVariable Long userId) {
        Optional<UserNotification> userNotification = userNotificationRepository.findByUserId(userId);
        return userNotification.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Récupère les préférences par email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserNotification> getUserNotificationByEmail(@PathVariable String email) {
        Optional<UserNotification> userNotification = userNotificationRepository.findByEmail(email);
        return userNotification.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Crée ou met à jour les préférences de notification d'un utilisateur
     */
    @PostMapping
    public ResponseEntity<UserNotification> createOrUpdateUserNotification(
            @RequestBody UserNotification userNotification) {
        try {
            // Vérifier si l'utilisateur existe déjà
            Optional<UserNotification> existing = userNotificationRepository
                .findByUserId(userNotification.getUserId());
            
            if (existing.isPresent()) {
                userNotification.setId(existing.get().getId());
            }
            
            UserNotification saved = userNotificationRepository.save(userNotification);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde des préférences: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Met à jour les préférences de notification
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserNotification> updateUserNotification(
            @PathVariable Long id,
            @RequestBody UserNotification userNotification) {
        Optional<UserNotification> existing = userNotificationRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        userNotification.setId(id);
        UserNotification updated = userNotificationRepository.save(userNotification);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Met à jour les préférences de notification d'un utilisateur
     */
    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserNotification> updateUserNotificationPreferences(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean wantsPromoNotifications,
            @RequestParam(required = false) Boolean wantsAvailabilityNotifications) {
        Optional<UserNotification> userNotificationOpt = userNotificationRepository.findByUserId(userId);
        
        UserNotification userNotification;
        if (userNotificationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            userNotification = userNotificationOpt.get();
        }
        
        if (wantsPromoNotifications != null) {
            userNotification.setWantsPromoNotifications(wantsPromoNotifications);
        }
        if (wantsAvailabilityNotifications != null) {
            userNotification.setWantsAvailabilityNotifications(wantsAvailabilityNotifications);
        }
        
        UserNotification updated = userNotificationRepository.save(userNotification);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Supprime les préférences de notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserNotification(@PathVariable Long id) {
        if (userNotificationRepository.existsById(id)) {
            userNotificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

