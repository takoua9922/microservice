package tn.esprit.ecommerce.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String recipientEmail;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    
    @Column(nullable = false)
    private LocalDateTime sentAt;
    
    @Column(nullable = false)
    private Boolean isSent = false;
    
    private Long userId;
    
    private Long productId;
    
    private Long promotionId;
    
    public enum NotificationType {
        PROMOTION,
        PRODUCT_AVAILABILITY,
        GENERAL
    }
}

