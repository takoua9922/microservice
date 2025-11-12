# 🧪 Guide de Test Complet - Système de Notifications E-Commerce

## 📋 Table des Matières

1. [Prérequis](#prérequis)
2. [Scénarios de Test](#scénarios-de-test)
3. [Tests par Fonctionnalité](#tests-par-fonctionnalité)
4. [Tests d'Intégration](#tests-dintégration)
5. [Tests WebSocket](#tests-websocket)
6. [Scripts de Test](#scripts-de-test)

---

## 🔧 Prérequis

### Services à démarrer
1. ✅ **MySQL** - Base de données DB5
2. ✅ **E-Commerce Service** - Port 8085
3. ✅ **Eureka Server** (optionnel) - Port 8761
4. ✅ **API Gateway** (optionnel) - Port 8081

### URLs importantes
- **Service Direct** : http://localhost:8085
- **Swagger UI** : http://localhost:8085/swagger-ui.html
- **Via API Gateway** : http://localhost:8081/api/notifications
- **WebSocket** : ws://localhost:8085/ws

### Tokens de test disponibles
- `valid-token-12345` → userId: 1
- `token-user-2` → userId: 2
- `token-user-3` → userId: 3

---

## 🎯 Scénarios de Test

### Scénario 1 : Configuration Initiale - Créer un Utilisateur avec Préférences

**Objectif** : Configurer un utilisateur pour recevoir des notifications

**Étapes** :

1. **Créer les préférences de notification d'un utilisateur**
```bash
POST http://localhost:8085/api/user-notifications
Content-Type: application/json

{
  "userId": 1,
  "email": "user1@example.com",
  "wantsPromoNotifications": true,
  "wantsAvailabilityNotifications": true
}
```

**Résultat attendu** :
- ✅ Status 201 Created
- ✅ Retourne l'objet UserNotification créé
- ✅ `wantsPromoNotifications` = true
- ✅ `wantsAvailabilityNotifications` = true

2. **Vérifier les préférences créées**
```bash
GET http://localhost:8085/api/user-notifications/user/1
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Retourne les préférences de l'utilisateur 1

---

### Scénario 2 : Notification Personnalisée avec Authentification

**Objectif** : Envoyer une notification personnalisée avec validation de token

**Étapes** :

1. **Valider un token d'authentification**
```bash
POST http://localhost:8085/api/auth/validate
Content-Type: application/json

{
  "token": "valid-token-12345"
}
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"valid": true, "userId": 1}`

2. **Envoyer une notification personnalisée (avec token dans header)**
```bash
POST http://localhost:8085/api/notifications/send
Authorization: Bearer valid-token-12345
Content-Type: application/json

{
  "recipientEmail": "user1@example.com",
  "subject": "Bienvenue sur TuniShop!",
  "message": "Merci de vous être inscrit. Profitez de nos offres spéciales!"
}
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Retourne l'objet Notification créé
- ✅ Email envoyé à `user1@example.com`
- ✅ Notification sauvegardée en base de données
- ✅ Notification envoyée via WebSocket

3. **Tester avec token invalide**
```bash
POST http://localhost:8085/api/notifications/send
Authorization: Bearer invalid-token
Content-Type: application/json

{
  "recipientEmail": "user1@example.com",
  "subject": "Test",
  "message": "Test message"
}
```

**Résultat attendu** :
- ✅ Status 401 Unauthorized
- ✅ `{"error": "Token d'authentification invalide"}`

4. **Tester sans token**
```bash
POST http://localhost:8085/api/notifications/send
Content-Type: application/json

{
  "recipientEmail": "user1@example.com",
  "subject": "Test",
  "message": "Test message"
}
```

**Résultat attendu** :
- ✅ Status 401 Unauthorized
- ✅ Message d'erreur indiquant que le token est requis

---

### Scénario 3 : Notifications de Promotions

**Objectif** : Tester le système de notifications de promotions

**Étapes** :

1. **Créer une promotion active**
```bash
POST http://localhost:8085/api/promotions
Content-Type: application/json

{
  "name": "Promo Été 2024",
  "discountPercentage": 25.0,
  "startDate": "2024-06-01T00:00:00",
  "endDate": "2024-08-31T23:59:59",
  "isActive": true
}
```

**Résultat attendu** :
- ✅ Status 201 Created
- ✅ Retourne la promotion créée avec un ID

2. **Vérifier les promotions actives**
```bash
GET http://localhost:8085/api/promotions/active
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Liste des promotions avec `isActive = true`

3. **Envoyer des notifications pour une promotion**
```bash
POST http://localhost:8085/api/notifications/promotions/1/notify
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"message": "Notifications de promotion envoyées avec succès", "promotionId": 1}`
- ✅ Emails envoyés à tous les utilisateurs avec `wantsPromoNotifications = true`
- ✅ Notifications sauvegardées en base de données
- ✅ Notifications envoyées via WebSocket

4. **Vérifier les notifications envoyées**
```bash
GET http://localhost:8085/api/notifications/email/user1@example.com
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Liste des notifications pour cet email
- ✅ Au moins une notification de type PROMOTION

5. **Vérifier les notifications par utilisateur**
```bash
GET http://localhost:8085/api/notifications/user/1
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Liste des notifications pour l'utilisateur 1

6. **Déclencher la vérification automatique des promotions actives**
```bash
POST http://localhost:8085/api/notifications/promotions/check-active
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"message": "Vérification des promotions actives lancée"}`
- ✅ Les promotions actives sont détectées et les notifications envoyées

---

### Scénario 4 : Notifications de Disponibilité de Produits

**Objectif** : Tester les notifications de disponibilité et rupture de stock

**Étapes** :

1. **Créer un produit disponible**
```bash
POST http://localhost:8085/api/products
Content-Type: application/json

{
  "name": "iPhone 15 Pro",
  "price": 1299.99,
  "stockQuantity": 10,
  "isAvailable": true
}
```

**Résultat attendu** :
- ✅ Status 201 Created
- ✅ Retourne le produit créé avec un ID

2. **Notifier la disponibilité d'un produit**
```bash
POST http://localhost:8085/api/notifications/products/1/availability?email=user1@example.com
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"message": "Notification de disponibilité envoyée avec succès", "productId": 1, "email": "user1@example.com"}`
- ✅ Email envoyé à l'utilisateur
- ✅ Notification sauvegardée

3. **Mettre un produit en rupture de stock**
```bash
PATCH http://localhost:8085/api/products/1/availability?isAvailable=false&stockQuantity=0
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Produit mis à jour avec `isAvailable = false` et `stockQuantity = 0`

4. **Notifier la rupture de stock**
```bash
POST http://localhost:8085/api/notifications/products/1/out-of-stock?email=user1@example.com
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"message": "Notification de rupture de stock envoyée avec succès", "productId": 1, "email": "user1@example.com"}`
- ✅ Email de rupture de stock envoyé

5. **Remettre le produit en stock**
```bash
PATCH http://localhost:8085/api/products/1/availability?isAvailable=true&stockQuantity=5
```

6. **Déclencher la vérification automatique de disponibilité**
```bash
POST http://localhost:8085/api/notifications/products/check-availability
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ `{"message": "Vérification de disponibilité des produits lancée"}`
- ✅ Les produits disponibles sont détectés et les notifications envoyées

---

### Scénario 5 : Gestion des Préférences Utilisateur

**Objectif** : Tester la gestion des préférences de notifications

**Étapes** :

1. **Créer plusieurs utilisateurs avec différentes préférences**
```bash
# Utilisateur 1 - Veut toutes les notifications
POST http://localhost:8085/api/user-notifications
Content-Type: application/json

{
  "userId": 1,
  "email": "user1@example.com",
  "wantsPromoNotifications": true,
  "wantsAvailabilityNotifications": true
}

# Utilisateur 2 - Veut seulement les promotions
POST http://localhost:8085/api/user-notifications
Content-Type: application/json

{
  "userId": 2,
  "email": "user2@example.com",
  "wantsPromoNotifications": true,
  "wantsAvailabilityNotifications": false
}

# Utilisateur 3 - Ne veut aucune notification
POST http://localhost:8085/api/user-notifications
Content-Type: application/json

{
  "userId": 3,
  "email": "user3@example.com",
  "wantsPromoNotifications": false,
  "wantsAvailabilityNotifications": false
}
```

2. **Modifier les préférences d'un utilisateur**
```bash
PATCH http://localhost:8085/api/user-notifications/user/2?wantsAvailabilityNotifications=true
```

**Résultat attendu** :
- ✅ Status 200 OK
- ✅ Préférences mises à jour

3. **Vérifier que seuls les utilisateurs intéressés reçoivent les notifications**
   - Créer une promotion et envoyer les notifications
   - Vérifier que user1 et user2 reçoivent, mais pas user3

---

### Scénario 6 : Prévention des Doublons (24h)

**Objectif** : Vérifier que les notifications ne sont pas envoyées en double dans les 24h

**Étapes** :

1. **Envoyer une notification de promotion**
```bash
POST http://localhost:8085/api/notifications/promotions/1/notify
```

2. **Réessayer immédiatement**
```bash
POST http://localhost:8085/api/notifications/promotions/1/notify
```

**Résultat attendu** :
- ✅ La deuxième notification ne doit pas être envoyée (vérification dans les logs)
- ✅ Seule la première notification est enregistrée dans les dernières 24h

3. **Vérifier les notifications récentes**
```bash
GET http://localhost:8085/api/notifications/user/1
```

---

### Scénario 7 : Tests d'Erreurs et Cas Limites

**Objectif** : Tester la gestion des erreurs

**Étapes** :

1. **Notification pour une promotion inexistante**
```bash
POST http://localhost:8085/api/notifications/promotions/999/notify
```

**Résultat attendu** :
- ✅ Status 200 OK (pas d'erreur, mais aucun email envoyé)
- ✅ Logs indiquent que la promotion n'existe pas

2. **Notification pour un produit inexistant**
```bash
POST http://localhost:8085/api/notifications/products/999/availability?email=user1@example.com
```

**Résultat attendu** :
- ✅ Status 200 OK (pas d'erreur, mais aucun email envoyé)

3. **Notification pour un utilisateur sans préférences**
```bash
POST http://localhost:8085/api/notifications/products/1/availability?email=unknown@example.com
```

**Résultat attendu** :
- ✅ Status 200 OK (pas d'erreur, mais aucun email envoyé)
- ✅ Logs indiquent que l'utilisateur ne souhaite pas recevoir de notifications

---

## 🔌 Tests WebSocket

### Scénario 8 : Notifications en Temps Réel via WebSocket

**Objectif** : Tester les notifications WebSocket

**Méthode 1 : Utiliser la page de test HTML**

1. **Ouvrir la page de test**
```
http://localhost:8085/websocket-test.html
```

2. **Se connecter au WebSocket**
   - Cliquer sur "Connect"
   - Vérifier que la connexion est établie

3. **S'abonner aux topics**
   - `/topic/notifications` - Toutes les notifications
   - `/topic/promotions` - Notifications de promotions
   - `/topic/products` - Notifications de produits

4. **Déclencher une notification**
```bash
POST http://localhost:8085/api/notifications/promotions/1/notify
```

5. **Vérifier que la notification arrive en temps réel**
   - La notification doit apparaître dans la page WebSocket

**Méthode 2 : Utiliser JavaScript dans la console**

```javascript
// Connexion WebSocket
const socket = new SockJS('http://localhost:8085/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    
    // S'abonner aux notifications
    stompClient.subscribe('/topic/notifications', function(message) {
        const notification = JSON.parse(message.body);
        console.log('Notification reçue:', notification);
    });
    
    // S'abonner aux promotions
    stompClient.subscribe('/topic/promotions', function(message) {
        const promotion = JSON.parse(message.body);
        console.log('Promotion reçue:', promotion);
    });
});
```

---

## 📊 Tests par Fonctionnalité

### 1. CRUD Promotions

```bash
# GET - Liste toutes les promotions
GET http://localhost:8085/api/promotions

# GET - Promotions actives
GET http://localhost:8085/api/promotions/active

# GET - Promotion par ID
GET http://localhost:8085/api/promotions/1

# POST - Créer une promotion
POST http://localhost:8085/api/promotions
Content-Type: application/json
{
  "name": "Promo Test",
  "discountPercentage": 15.0,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "isActive": true
}

# PUT - Mettre à jour une promotion
PUT http://localhost:8085/api/promotions/1
Content-Type: application/json
{
  "name": "Promo Test Modifiée",
  "discountPercentage": 20.0,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "isActive": true
}

# DELETE - Supprimer une promotion
DELETE http://localhost:8085/api/promotions/1
```

### 2. CRUD Produits

```bash
# GET - Liste tous les produits
GET http://localhost:8085/api/products

# GET - Produits disponibles
GET http://localhost:8085/api/products/available

# GET - Produit par ID
GET http://localhost:8085/api/products/1

# POST - Créer un produit
POST http://localhost:8085/api/products
Content-Type: application/json
{
  "name": "Produit Test",
  "price": 99.99,
  "stockQuantity": 10,
  "isAvailable": true
}

# PUT - Mettre à jour un produit
PUT http://localhost:8085/api/products/1
Content-Type: application/json
{
  "name": "Produit Test Modifié",
  "price": 89.99,
  "stockQuantity": 5,
  "isAvailable": true
}

# PATCH - Mettre à jour la disponibilité
PATCH http://localhost:8085/api/products/1/availability?isAvailable=false&stockQuantity=0

# DELETE - Supprimer un produit
DELETE http://localhost:8085/api/products/1
```

### 3. CRUD Préférences Utilisateur

```bash
# GET - Toutes les préférences
GET http://localhost:8085/api/user-notifications

# GET - Préférences par userId
GET http://localhost:8085/api/user-notifications/user/1

# GET - Préférences par email
GET http://localhost:8085/api/user-notifications/email/user1@example.com

# POST - Créer/Mettre à jour préférences
POST http://localhost:8085/api/user-notifications
Content-Type: application/json
{
  "userId": 1,
  "email": "user1@example.com",
  "wantsPromoNotifications": true,
  "wantsAvailabilityNotifications": true
}

# PUT - Mettre à jour préférences
PUT http://localhost:8085/api/user-notifications/1
Content-Type: application/json
{
  "userId": 1,
  "email": "user1@example.com",
  "wantsPromoNotifications": false,
  "wantsAvailabilityNotifications": true
}

# PATCH - Mettre à jour partiellement
PATCH http://localhost:8085/api/user-notifications/user/1?wantsPromoNotifications=false

# DELETE - Supprimer préférences
DELETE http://localhost:8085/api/user-notifications/1
```

### 4. Authentification

```bash
# POST - Valider un token
POST http://localhost:8085/api/auth/validate
Content-Type: application/json
{
  "token": "valid-token-12345"
}

# POST - Ajouter un token (développement)
POST http://localhost:8085/api/auth/tokens
Content-Type: application/json
{
  "token": "new-token-123",
  "userId": 4
}
```

---

## 🧪 Tests d'Intégration Complets

### Test Complet End-to-End

**Scénario** : Un utilisateur s'inscrit, configure ses préférences, et reçoit des notifications

1. **Créer un utilisateur avec préférences**
2. **Créer une promotion active**
3. **Envoyer les notifications de promotion**
4. **Vérifier que l'utilisateur a reçu l'email**
5. **Vérifier que la notification est enregistrée**
6. **Créer un produit**
7. **Notifier la disponibilité**
8. **Vérifier les notifications de l'utilisateur**
9. **Tester via WebSocket en temps réel**

---

## 📝 Scripts de Test PowerShell

### Script 1 : Configuration Initiale

```powershell
# Créer un utilisateur avec préférences
$body = @{
    userId = 1
    email = "user1@example.com"
    wantsPromoNotifications = $true
    wantsAvailabilityNotifications = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8085/api/user-notifications" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Script 2 : Créer une Promotion et Notifier

```powershell
# Créer une promotion
$promoBody = @{
    name = "Promo Test"
    discountPercentage = 20.0
    startDate = "2024-01-01T00:00:00"
    endDate = "2024-12-31T23:59:59"
    isActive = $true
} | ConvertTo-Json

$promo = Invoke-RestMethod -Uri "http://localhost:8085/api/promotions" `
    -Method POST `
    -ContentType "application/json" `
    -Body $promoBody

# Notifier la promotion
Invoke-RestMethod -Uri "http://localhost:8085/api/notifications/promotions/$($promo.id)/notify" `
    -Method POST
```

### Script 3 : Test d'Authentification

```powershell
# Valider un token
$tokenBody = @{
    token = "valid-token-12345"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8085/api/auth/validate" `
    -Method POST `
    -ContentType "application/json" `
    -Body $tokenBody

# Envoyer une notification avec token
$headers = @{
    Authorization = "Bearer valid-token-12345"
}

$notifBody = @{
    recipientEmail = "user1@example.com"
    subject = "Test Notification"
    message = "Ceci est un test"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8085/api/notifications/send" `
    -Method POST `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $notifBody
```

---

## ✅ Checklist de Test

### Fonctionnalités de Base
- [ ] Créer un utilisateur avec préférences
- [ ] Modifier les préférences d'un utilisateur
- [ ] Créer une promotion
- [ ] Créer un produit
- [ ] Mettre à jour la disponibilité d'un produit

### Notifications
- [ ] Envoyer une notification personnalisée avec token valide
- [ ] Tester avec token invalide (doit échouer)
- [ ] Envoyer des notifications de promotion
- [ ] Envoyer des notifications de disponibilité
- [ ] Envoyer des notifications de rupture de stock
- [ ] Vérifier la prévention des doublons (24h)

### Authentification
- [ ] Valider un token valide
- [ ] Valider un token invalide
- [ ] Ajouter un nouveau token

### WebSocket
- [ ] Se connecter au WebSocket
- [ ] S'abonner aux notifications
- [ ] Recevoir des notifications en temps réel

### Vérifications
- [ ] Récupérer les notifications d'un utilisateur
- [ ] Récupérer les notifications par email
- [ ] Vérifier les promotions actives
- [ ] Vérifier les produits disponibles

---

## 🐛 Dépannage

### Problème : Les emails ne sont pas envoyés

**Solutions** :
1. Vérifier la configuration SMTP dans `application.properties`
2. Vérifier les logs pour les erreurs d'envoi
3. Utiliser MailHog ou Mailtrap pour les tests locaux

### Problème : Les notifications ne sont pas sauvegardées

**Solutions** :
1. Vérifier la connexion à la base de données MySQL
2. Vérifier que la table `notifications` existe
3. Vérifier les logs pour les erreurs de base de données

### Problème : WebSocket ne fonctionne pas

**Solutions** :
1. Vérifier que le service est démarré sur le port 8085
2. Utiliser la page de test : http://localhost:8085/websocket-test.html
3. Vérifier la console du navigateur pour les erreurs

---

## 📚 Ressources

- **Swagger UI** : http://localhost:8085/swagger-ui.html
- **Documentation API** : http://localhost:8085/api-docs
- **Page de test WebSocket** : http://localhost:8085/websocket-test.html

