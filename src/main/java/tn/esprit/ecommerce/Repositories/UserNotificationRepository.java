package tn.esprit.ecommerce.Repositories;

import tn.esprit.ecommerce.Entities.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    
    Optional<UserNotification> findByUserId(Long userId);
    
    Optional<UserNotification> findByEmail(String email);
    
    List<UserNotification> findByWantsPromoNotificationsTrue();
    
    List<UserNotification> findByWantsAvailabilityNotificationsTrue();
}

