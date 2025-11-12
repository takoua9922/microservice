package tn.esprit.ecommerce.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    /**
     * Envoie un email simple
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            // Utiliser l'email configuré ou un email par défaut
            String senderEmail = (fromEmail != null && !fromEmail.isEmpty()) ? fromEmail : "noreply@tunishop.com";
            message.setFrom(senderEmail);
            
            mailSender.send(message);
            log.info("✅ Email envoyé avec succès de {} à: {}", senderEmail, to);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            log.error("Détails de l'erreur: ", e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Envoie un email HTML
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            // Utiliser l'email configuré ou un email par défaut
            String senderEmail = (fromEmail != null && !fromEmail.isEmpty()) ? fromEmail : "noreply@tunishop.com";
            helper.setFrom(senderEmail);
            
            mailSender.send(message);
            log.info("✅ Email HTML envoyé avec succès de {} à: {}", senderEmail, to);
        } catch (MessagingException e) {
            log.error("❌ Erreur lors de l'envoi de l'email HTML à {}: {}", to, e.getMessage());
            log.error("Détails de l'erreur: ", e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email HTML: " + e.getMessage(), e);
        }
    }
    
    /**
     * Envoie une notification de promotion
     */
    public void sendPromotionNotification(String to, String promotionName, Double discount) {
        String subject = "🎉 Nouvelle Promotion: " + promotionName;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>Promotion Spéciale!</h2>" +
            "<p>Bonjour,</p>" +
            "<p>Nous avons une promotion active: <strong>%s</strong></p>" +
            "<p>Réduction de <strong>%.0f%%</strong>!</p>" +
            "<p>Ne manquez pas cette offre!</p>" +
            "<p>Cordialement,<br>L'équipe Tuni Shop</p>" +
            "</body></html>",
            promotionName, discount
        );
        sendHtmlEmail(to, subject, htmlContent);
    }
    
    /**
     * Envoie une notification de disponibilité de produit
     */
    public void sendProductAvailabilityNotification(String to, String productName) {
        String subject = "✅ Produit Disponible: " + productName;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>Produit Disponible!</h2>" +
            "<p>Bonjour,</p>" +
            "<p>Le produit <strong>%s</strong> est maintenant disponible!</p>" +
            "<p>Dépêchez-vous de le commander avant qu'il ne soit à nouveau en rupture de stock.</p>" +
            "<p>Cordialement,<br>L'équipe Tuni Shop</p>" +
            "</body></html>",
            productName
        );
        sendHtmlEmail(to, subject, htmlContent);
    }
    
    /**
     * Envoie une notification de rupture de stock
     */
    public void sendProductOutOfStockNotification(String to, String productName) {
        String subject = "⚠️ Produit en Rupture de Stock: " + productName;
        String htmlContent = String.format(
            "<html><body>" +
            "<h2>Produit en Rupture de Stock</h2>" +
            "<p>Bonjour,</p>" +
            "<p>Nous vous informons que le produit <strong>%s</strong> est actuellement en rupture de stock.</p>" +
            "<p>Nous vous notifierons dès qu'il sera à nouveau disponible.</p>" +
            "<p>Cordialement,<br>L'équipe Tuni Shop</p>" +
            "</body></html>",
            productName
        );
        sendHtmlEmail(to, subject, htmlContent);
    }
}

