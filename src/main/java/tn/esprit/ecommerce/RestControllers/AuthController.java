package tn.esprit.ecommerce.RestControllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ecommerce.Services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * Valide un token d'authentification
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("valid", false, "error", "Token requis"));
        }
        
        Long userId = authService.validateToken(token);
        if (userId != null) {
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "userId", userId
            ));
        }
        
        return ResponseEntity.ok(Map.of("valid", false));
    }
    
    /**
     * Ajoute un token de test (pour développement)
     */
    @PostMapping("/tokens")
    public ResponseEntity<?> addToken(@RequestBody Map<String, Object> request) {
        String token = (String) request.get("token");
        Long userId = ((Number) request.get("userId")).longValue();
        
        if (token == null || userId == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "token et userId sont requis"));
        }
        
        authService.addToken(token, userId);
        return ResponseEntity.ok(Map.of(
            "message", "Token ajouté avec succès",
            "token", token,
            "userId", userId
        ));
    }
}

