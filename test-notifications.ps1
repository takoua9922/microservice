# Script de Test - Système de Notifications E-Commerce
# Usage: .\test-notifications.ps1

$baseUrl = "http://localhost:8085"
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests du Système de Notifications" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Fonction pour afficher les résultats
function Show-Result {
    param($testName, $success, $message = "")
    if ($success) {
        Write-Host "[✓] $testName" -ForegroundColor Green
    } else {
        Write-Host "[✗] $testName" -ForegroundColor Red
        if ($message) {
            Write-Host "   $message" -ForegroundColor Yellow
        }
    }
}

# Test 1: Créer un utilisateur avec préférences
Write-Host "Test 1: Création d'un utilisateur avec préférences..." -ForegroundColor Yellow
try {
    $userBody = @{
        userId = 1
        email = "user1@example.com"
        wantsPromoNotifications = $true
        wantsAvailabilityNotifications = $true
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/user-notifications" `
        -Method POST `
        -Headers $headers `
        -Body $userBody
    
    Show-Result "Création utilisateur" $true
    Write-Host "   User ID: $($response.userId), Email: $($response.email)" -ForegroundColor Gray
} catch {
    Show-Result "Création utilisateur" $false $_.Exception.Message
}
Write-Host ""

# Test 2: Valider un token
Write-Host "Test 2: Validation d'un token..." -ForegroundColor Yellow
try {
    $tokenBody = @{
        token = "valid-token-12345"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/validate" `
        -Method POST `
        -Headers $headers `
        -Body $tokenBody
    
    if ($response.valid -eq $true) {
        Show-Result "Validation token" $true
        Write-Host "   User ID: $($response.userId)" -ForegroundColor Gray
    } else {
        Show-Result "Validation token" $false "Token invalide"
    }
} catch {
    Show-Result "Validation token" $false $_.Exception.Message
}
Write-Host ""

# Test 3: Créer une promotion
Write-Host "Test 3: Création d'une promotion..." -ForegroundColor Yellow
try {
    $promoBody = @{
        name = "Promo Test Automatique"
        discountPercentage = 25.0
        startDate = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ss")
        endDate = (Get-Date).AddMonths(3).ToString("yyyy-MM-ddTHH:mm:ss")
        isActive = $true
    } | ConvertTo-Json

    $promo = Invoke-RestMethod -Uri "$baseUrl/api/promotions" `
        -Method POST `
        -Headers $headers `
        -Body $promoBody
    
    Show-Result "Création promotion" $true
    Write-Host "   Promotion ID: $($promo.id), Nom: $($promo.name)" -ForegroundColor Gray
    $global:promoId = $promo.id
} catch {
    Show-Result "Création promotion" $false $_.Exception.Message
    $global:promoId = 1
}
Write-Host ""

# Test 4: Envoyer des notifications de promotion
Write-Host "Test 4: Envoi de notifications de promotion..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/notifications/promotions/$global:promoId/notify" `
        -Method POST
    
    Show-Result "Envoi notifications promotion" $true
    Write-Host "   Message: $($response.message)" -ForegroundColor Gray
} catch {
    Show-Result "Envoi notifications promotion" $false $_.Exception.Message
}
Write-Host ""

# Test 5: Créer un produit
Write-Host "Test 5: Création d'un produit..." -ForegroundColor Yellow
try {
    $productBody = @{
        name = "Produit Test Automatique"
        price = 99.99
        stockQuantity = 10
        isAvailable = $true
    } | ConvertTo-Json

    $product = Invoke-RestMethod -Uri "$baseUrl/api/products" `
        -Method POST `
        -Headers $headers `
        -Body $productBody
    
    Show-Result "Création produit" $true
    Write-Host "   Produit ID: $($product.id), Nom: $($product.name)" -ForegroundColor Gray
    $global:productId = $product.id
} catch {
    Show-Result "Création produit" $false $_.Exception.Message
    $global:productId = 1
}
Write-Host ""

# Test 6: Notifier la disponibilité d'un produit
Write-Host "Test 6: Notification de disponibilité produit..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/notifications/products/$global:productId/availability?email=user1@example.com" `
        -Method POST
    
    Show-Result "Notification disponibilité" $true
    Write-Host "   Message: $($response.message)" -ForegroundColor Gray
} catch {
    Show-Result "Notification disponibilité" $false $_.Exception.Message
}
Write-Host ""

# Test 7: Envoyer une notification personnalisée avec token
Write-Host "Test 7: Notification personnalisée avec authentification..." -ForegroundColor Yellow
try {
    $authHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer valid-token-12345"
    }

    $notifBody = @{
        recipientEmail = "user1@example.com"
        subject = "Test Automatique - Notification Personnalisée"
        message = "Ceci est une notification de test envoyée automatiquement."
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/notifications/send" `
        -Method POST `
        -Headers $authHeaders `
        -Body $notifBody
    
    Show-Result "Notification personnalisée" $true
    Write-Host "   Notification ID: $($response.id)" -ForegroundColor Gray
} catch {
    Show-Result "Notification personnalisée" $false $_.Exception.Message
}
Write-Host ""

# Test 8: Récupérer les notifications d'un utilisateur
Write-Host "Test 8: Récupération des notifications utilisateur..." -ForegroundColor Yellow
try {
    $notifications = Invoke-RestMethod -Uri "$baseUrl/api/notifications/user/1" `
        -Method GET
    
    Show-Result "Récupération notifications" $true
    Write-Host "   Nombre de notifications: $($notifications.Count)" -ForegroundColor Gray
    if ($notifications.Count -gt 0) {
        Write-Host "   Dernière notification: $($notifications[0].subject)" -ForegroundColor Gray
    }
} catch {
    Show-Result "Récupération notifications" $false $_.Exception.Message
}
Write-Host ""

# Test 9: Vérifier les promotions actives
Write-Host "Test 9: Vérification des promotions actives..." -ForegroundColor Yellow
try {
    $promotions = Invoke-RestMethod -Uri "$baseUrl/api/promotions/active" `
        -Method GET
    
    Show-Result "Promotions actives" $true
    Write-Host "   Nombre de promotions actives: $($promotions.Count)" -ForegroundColor Gray
} catch {
    Show-Result "Promotions actives" $false $_.Exception.Message
}
Write-Host ""

# Test 10: Vérifier les produits disponibles
Write-Host "Test 10: Vérification des produits disponibles..." -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/products/available" `
        -Method GET
    
    Show-Result "Produits disponibles" $true
    Write-Host "   Nombre de produits disponibles: $($products.Count)" -ForegroundColor Gray
} catch {
    Show-Result "Produits disponibles" $false $_.Exception.Message
}
Write-Host ""

# Test 11: Test avec token invalide (doit échouer)
Write-Host "Test 11: Test avec token invalide (doit échouer)..." -ForegroundColor Yellow
try {
    $authHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer invalid-token"
    }

    $notifBody = @{
        recipientEmail = "user1@example.com"
        subject = "Test"
        message = "Test"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$baseUrl/api/notifications/send" `
        -Method POST `
        -Headers $authHeaders `
        -Body $notifBody
    
    Show-Result "Test token invalide" $false "Devrait échouer mais a réussi"
} catch {
    Show-Result "Test token invalide" $true "Échec attendu avec token invalide"
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests terminés!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Pour tester WebSocket, ouvrez:" -ForegroundColor Yellow
Write-Host "http://localhost:8085/websocket-test.html" -ForegroundColor White
Write-Host ""
Write-Host "Pour voir la documentation API:" -ForegroundColor Yellow
Write-Host "http://localhost:8085/swagger-ui.html" -ForegroundColor White

