# Microservice Gestion Notifications - Tuni Shop

## Description
Microservice de gestion des notifications pour l'application e-commerce Tuni Shop. Ce service gère l'envoi automatique d'emails pour les promotions et la disponibilité des produits.

**Développeur:** amine  
**Technologie:** Spring Boot + MySQL  
**Port:** 8085  
**Base de données:** DB5 (MySQL)

## Fonctionnalités

### ✅ Notifications par email
- Envoi automatique d'emails pour les promotions actives
- Notifications de disponibilité de produits
- Support des emails HTML

### ✅ Authentification et validation
- Vérification de l'authentification utilisateur avant envoi
- Système de token simple (simulation)
- Validation des tokens via API

### ✅ Notifications des promos
- Alerte automatique sur les promotions actives
- Vérification périodique (toutes les heures)
- Envoi aux utilisateurs intéressés

### ✅ Notification de disponibilité de produit
- Alerte quand un produit redevient disponible
- Alerte en cas de rupture de stock
- Vérification périodique (toutes les 30 minutes)

## Architecture

```
Entities/
├── Notification          # Entité principale pour les notifications
├── Promotion             # Entité pour les promotions
├── Product               # Entité pour les produits
└── UserNotification      # Préférences de notification des utilisateurs

Repositories/
├── NotificationRepository
├── PromotionRepository
├── ProductRepository
└── UserNotificationRepository

Services/
├── EmailService              # Service d'envoi d'emails
├── AuthService               # Service de validation de tokens
├── NotificationService       # Service principal de gestion des notifications
└── ScheduledNotificationService  # Service de vérification automatique

RestControllers/
├── NotificationController     # API pour les notifications
├── PromotionController        # API pour les promotions
├── ProductController          # API pour les produits
├── UserNotificationController # API pour les préférences utilisateurs
└── AuthController            # API pour l'authentification
```

## Configuration

### Base de données MySQL
- **Nom de la base:** DB5
- **Port:** 3306
- **Utilisateur:** root
- **Mot de passe:** (à configurer)

### Configuration Email
Modifier dans `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Note:** Pour Gmail, vous devez utiliser un "App Password" et non votre mot de passe habituel.

### Service Discovery (Eureka)
- **URL Eureka:** http://localhost:8761/eureka/
- Le service s'enregistre automatiquement auprès d'Eureka

### Config Server
- **URL Config Server:** http://localhost:8888
- Configuration centralisée (optionnelle)

## API Endpoints

### Notifications
- `POST /api/notifications/send` - Envoyer une notification personnalisée (requiert token)
- `POST /api/notifications/promotions/{id}/notify` - Notifier une promotion
- `POST /api/notifications/products/{id}/availability` - Notifier disponibilité produit
- `GET /api/notifications/user/{userId}` - Récupérer notifications d'un utilisateur
- `GET /api/notifications/email/{email}` - Récupérer notifications par email
- `POST /api/notifications/promotions/check-active` - Vérifier promotions actives
- `POST /api/notifications/products/check-availability` - Vérifier disponibilité produits

### Promotions
- `GET /api/promotions` - Liste toutes les promotions
- `GET /api/promotions/active` - Liste promotions actives
- `GET /api/promotions/{id}` - Détails d'une promotion
- `POST /api/promotions` - Créer une promotion
- `PUT /api/promotions/{id}` - Mettre à jour une promotion
- `DELETE /api/promotions/{id}` - Supprimer une promotion

### Produits
- `GET /api/products` - Liste tous les produits
- `GET /api/products/available` - Liste produits disponibles
- `GET /api/products/{id}` - Détails d'un produit
- `POST /api/products` - Créer un produit
- `PUT /api/products/{id}` - Mettre à jour un produit
- `PATCH /api/products/{id}/availability` - Mettre à jour disponibilité
- `DELETE /api/products/{id}` - Supprimer un produit

### Préférences Utilisateur
- `GET /api/user-notifications` - Liste toutes les préférences
- `GET /api/user-notifications/user/{userId}` - Préférences d'un utilisateur
- `GET /api/user-notifications/email/{email}` - Préférences par email
- `POST /api/user-notifications` - Créer/mettre à jour préférences
- `PUT /api/user-notifications/{id}` - Mettre à jour préférences
- `PATCH /api/user-notifications/user/{userId}` - Mettre à jour préférences partielles
- `DELETE /api/user-notifications/{id}` - Supprimer préférences

### Authentification
- `POST /api/auth/validate` - Valider un token
- `POST /api/auth/tokens` - Ajouter un token (développement)

## Exemples d'utilisation

### 1. Créer une promotion et notifier
```bash
# Créer une promotion
curl -X POST http://localhost:8085/api/promotions \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Promo Été 2024",
    "description": "Réduction sur tous les produits",
    "discountPercentage": 20.0,
    "startDate": "2024-06-01T00:00:00",
    "endDate": "2024-08-31T23:59:59",
    "isActive": true
  }'

# Notifier les utilisateurs
curl -X POST http://localhost:8085/api/notifications/promotions/1/notify
```

### 2. Envoyer une notification personnalisée
```bash
curl -X POST http://localhost:8085/api/notifications/send \
  -H "Content-Type: application/json" \
  -H "Authorization: valid-token-12345" \
  -d '{
    "recipientEmail": "user@example.com",
    "subject": "Bienvenue!",
    "message": "Merci de vous être inscrit!"
  }'
```

### 3. Configurer les préférences d'un utilisateur
```bash
curl -X POST http://localhost:8085/api/user-notifications \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "email": "user@example.com",
    "wantsPromoNotifications": true,
    "wantsAvailabilityNotifications": true
  }'
```

### 4. Valider un token
```bash
curl -X POST http://localhost:8085/api/auth/validate \
  -H "Content-Type: application/json" \
  -d '{"token": "valid-token-12345"}'
```

## Tokens de test

Par défaut, les tokens suivants sont disponibles:
- `valid-token-12345` → userId: 1
- `token-user-2` → userId: 2
- `token-user-3` → userId: 3

Vous pouvez ajouter de nouveaux tokens via l'endpoint `/api/auth/tokens`.

## Tâches automatiques

Le service exécute automatiquement:
- **Vérification des promotions actives:** Toutes les heures
- **Vérification de disponibilité produits:** Toutes les 30 minutes

## Dépendances Maven

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Mail
- Spring Cloud Netflix Eureka Client
- MySQL Connector
- Lombok

## Démarrage

1. Assurez-vous que MySQL est démarré
2. Configurez les paramètres email dans `application.properties`
3. (Optionnel) Démarrez Eureka Server sur le port 8761
4. (Optionnel) Démarrez Config Server sur le port 8888
5. Exécutez: `mvn spring-boot:run`

## Notes importantes

- Les emails sont envoyés de manière asynchrone
- Les notifications sont enregistrées en base de données
- Le système de tokens est simplifié (à remplacer par JWT/OAuth2 en production)
- Pour les tests, vous pouvez utiliser MailHog ou Mailtrap au lieu d'un serveur SMTP réel

