package tn.esprit.ecommerce.Repositories;

import tn.esprit.ecommerce.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByRecipientEmail(String email);
    
    List<Notification> findByType(Notification.NotificationType type);
    
    List<Notification> findByIsSent(Boolean isSent);
    
    List<Notification> findByUserId(Long userId);
}

