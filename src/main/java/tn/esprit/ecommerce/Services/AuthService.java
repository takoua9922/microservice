package tn.esprit.ecommerce.Services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {
    
    // Simulation d'un système de tokens simple
    // En production, utiliser JWT ou OAuth2
    private static final Map<String, Long> validTokens = new HashMap<>();
    private static final String DEFAULT_TOKEN = "valid-token-12345";
    
    static {
        // Initialiser avec quelques tokens de test
        validTokens.put(DEFAULT_TOKEN, 1L); // userId = 1
        validTokens.put("token-user-2", 2L);
        validTokens.put("token-user-3", 3L);
    }
    
    /**
     * Valide un token d'authentification
     * @param token Le token à valider
     * @return L'ID de l'utilisateur si le token est valide, null sinon
     */
    public Long validateToken(String token) {
        if (token == null || token.isEmpty()) {
            log.warn("Token vide ou null");
            return null;
        }
        
        Long userId = validTokens.get(token);
        if (userId != null) {
            log.info("Token valide pour l'utilisateur: {}", userId);
            return userId;
        }
        
        log.warn("Token invalide: {}", token);
        return null;
    }
    
    /**
     * Vérifie si un token est valide
     */
    public boolean isTokenValid(String token) {
        return validateToken(token) != null;
    }
    
    /**
     * Ajoute un nouveau token (pour les tests)
     */
    public void addToken(String token, Long userId) {
        validTokens.put(token, userId);
        log.info("Token ajouté pour l'utilisateur: {}", userId);
    }
    
    /**
     * Supprime un token
     */
    public void removeToken(String token) {
        validTokens.remove(token);
        log.info("Token supprimé");
    }
}

