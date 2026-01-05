# Script di verifica sicurezza per GitHub
# Esegui questo script PRIMA di fare commit per verificare che non ci siano credenziali

Write-Host "🔍 Verifica Sicurezza GitHub..." -ForegroundColor Cyan
Write-Host ""

$foundIssues = $false

# Verifica 1: Cerca pattern di credenziali Google
Write-Host "Controllo 1: Credenziali Google OAuth..." -ForegroundColor Yellow
$googleSecrets = Select-String -Path "*.md", "*.properties", "*.yml", "*.ps1" -Pattern "GOCSPX-" -ErrorAction SilentlyContinue
if ($googleSecrets) {
    Write-Host "❌ ATTENZIONE! Trovati Google Client Secrets nei file:" -ForegroundColor Red
    $googleSecrets | ForEach-Object { Write-Host "   - $($_.Filename):$($_.LineNumber)" -ForegroundColor Red }
    $foundIssues = $true
}
else {
    Write-Host "✅ Nessun Google Client Secret trovato" -ForegroundColor Green
}

# Verifica 2: Cerca pattern di Client ID reali
Write-Host ""
Write-Host "Controllo 2: Google Client IDs..." -ForegroundColor Yellow
$clientIds = Select-String -Path "*.md", "*.properties", "*.yml", "*.ps1" -Pattern "\d{12}-[a-z0-9]{32}\.apps\.googleusercontent\.com" -ErrorAction SilentlyContinue
if ($clientIds) {
    Write-Host "❌ ATTENZIONE! Trovati Google Client IDs nei file:" -ForegroundColor Red
    $clientIds | ForEach-Object { Write-Host "   - $($_.Filename):$($_.LineNumber)" -ForegroundColor Red }
    $foundIssues = $true
}
else {
    Write-Host "✅ Nessun Google Client ID reale trovato" -ForegroundColor Green
}

# Verifica 3: Controlla .gitignore
Write-Host ""
Write-Host "Controllo 3: File .gitignore..." -ForegroundColor Yellow
if (Test-Path ".gitignore") {
    $gitignoreContent = Get-Content ".gitignore" -Raw
    if ($gitignoreContent -match "\.env" -and $gitignoreContent -match "credentials") {
        Write-Host "✅ .gitignore configurato correttamente" -ForegroundColor Green
    }
    else {
        Write-Host "⚠️  .gitignore potrebbe non essere completo" -ForegroundColor Yellow
        $foundIssues = $true
    }
}
else {
    Write-Host "❌ ATTENZIONE! File .gitignore non trovato" -ForegroundColor Red
    $foundIssues = $true
}

# Verifica 4: Controlla che non ci siano file .env tracciati
Write-Host ""
Write-Host "Controllo 4: File .env..." -ForegroundColor Yellow
if (Test-Path ".env") {
    Write-Host "⚠️  File .env trovato - assicurati che sia in .gitignore" -ForegroundColor Yellow
}
else {
    Write-Host "✅ Nessun file .env presente" -ForegroundColor Green
}

# Risultato finale
Write-Host ""
Write-Host "═══════════════════════════════════════" -ForegroundColor Cyan
if (-not $foundIssues) {
    Write-Host "✅ SICURO! Il progetto è pronto per GitHub" -ForegroundColor Green
    Write-Host ""
    Write-Host "Puoi procedere con:" -ForegroundColor Cyan
    Write-Host "  git init" -ForegroundColor White
    Write-Host "  git add ." -ForegroundColor White
    Write-Host "  git commit -m 'Initial commit'" -ForegroundColor White
    Write-Host "  git remote add origin <url-repository>" -ForegroundColor White
    Write-Host "  git push -u origin main" -ForegroundColor White
}
else {
    Write-Host "❌ ATTENZIONE! Problemi di sicurezza rilevati" -ForegroundColor Red
    Write-Host ""
    Write-Host "Risolvi i problemi sopra indicati prima di committare!" -ForegroundColor Yellow
    Write-Host "Consulta SECURITY.md per maggiori informazioni." -ForegroundColor Yellow
}
Write-Host "═══════════════════════════════════════" -ForegroundColor Cyan
