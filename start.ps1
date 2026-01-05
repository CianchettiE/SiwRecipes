# Script di avvio per SiwRecipes con Google OAuth configurato
# Uso: .\start.ps1

Write-Host "🚀 Configurazione Google OAuth..." -ForegroundColor Cyan

# Verifica se le credenziali sono già impostate
if (-not $env:GOOGLE_CLIENT_ID -or -not $env:GOOGLE_CLIENT_SECRET) {
    Write-Host "⚠️  Le credenziali Google OAuth non sono state trovate!" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Per configurarle, esegui questi comandi prima di avviare l'app:" -ForegroundColor Yellow
    Write-Host '  $env:GOOGLE_CLIENT_ID="your_client_id_here"' -ForegroundColor Cyan
    Write-Host '  $env:GOOGLE_CLIENT_SECRET="your_client_secret_here"' -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Oppure, se hai già le credenziali, premi CTRL+C per uscire e configurale manualmente." -ForegroundColor Yellow
    Write-Host ""
    
    # Chiedi all'utente se vuole continuare comunque
    $response = Read-Host "Vuoi continuare comunque? (s/n)"
    if ($response -ne "s" -and $response -ne "S") {
        Write-Host "❌ Avvio annullato." -ForegroundColor Red
        exit
    }
}

Write-Host "✅ Credenziali Google configurate" -ForegroundColor Green
Write-Host "🔧 Avvio dell'applicazione..." -ForegroundColor Cyan

# Avvia l'applicazione
./mvnw spring-boot:run
