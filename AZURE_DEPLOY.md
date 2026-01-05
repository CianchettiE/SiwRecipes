# Deploy su Azure Student - Guida Completa

## 🎓 Passo 1: Registrazione Azure Student

### Requisiti
- ✅ Email universitaria (@studenti.uniroma3.it o simile)
- ✅ Nessuna carta di credito richiesta
- ✅ $100 crediti gratuiti per 12 mesi

### Registrazione

1. **Vai su**: https://azure.microsoft.com/en-us/free/students/ (già aperto)
2. **Click** su "Activate now" o "Start free"
3. **Login** con Microsoft account o crea uno nuovo
4. **Verifica** con email universitaria
5. **Compila** il form studente
6. **Attendi** conferma (solitamente immediata)

---

## 🔧 Passo 2: Installa Azure CLI

### Download e Installazione

```powershell
# Download Azure CLI
start https://aka.ms/installazurecliwindows
```

**Oppure** con winget:
```powershell
winget install -e --id Microsoft.AzureCLI
```

### Verifica Installazione

Dopo l'installazione, **riavvia PowerShell** e verifica:
```powershell
az --version
```

---

## 🚀 Passo 3: Deploy dell'Applicazione

### 3.1 Login ad Azure

```powershell
cd c:\Users\cianc\Documents\workspace-spring-tools-for-eclipse-4.31.0.RELEASE\SiwRecipes
az login
```

Si aprirà il browser per il login. Usa le tue credenziali Azure Student.

### 3.2 Crea Resource Group

```powershell
az group create --name siwrecipes-rg --location westeurope
```

### 3.3 Crea PostgreSQL Database

```powershell
az postgres flexible-server create `
  --resource-group siwrecipes-rg `
  --name siwrecipes-db `
  --location westeurope `
  --admin-user siwadmin `
  --admin-password your_strong_password `
  --sku-name Standard_B1ms `
  --version 14 `
  --storage-size 32 `
  --public-access 0.0.0.0
```

**Nota**: Password: `your_strong_password` (salvala!)

### 3.4 Crea Database

```powershell
az postgres flexible-server db create `
  --resource-group siwrecipes-rg `
  --server-name siwrecipes-db `
  --database-name siwrecipes
```

### 3.5 Configura Firewall Database

```powershell
# Permetti connessioni da Azure services
az postgres flexible-server firewall-rule create `
  --resource-group siwrecipes-rg `
  --name siwrecipes-db `
  --rule-name AllowAzureServices `
  --start-ip-address 0.0.0.0 `
  --end-ip-address 0.0.0.0
```

### 3.6 Build dell'Applicazione

```powershell
.\mvnw.cmd clean package -DskipTests
```

### 3.7 Crea App Service Plan

```powershell
az appservice plan create `
  --name siwrecipes-plan `
  --resource-group siwrecipes-rg `
  --location westeurope `
  --sku B1 `
  --is-linux
```

### 3.8 Crea Web App

```powershell
az webapp create `
  --resource-group siwrecipes-rg `
  --plan siwrecipes-plan `
  --name siwrecipes-emanuele `
  --runtime "JAVA:17-java17"
```

**Nota**: Nome deve essere univoco. Se "siwrecipes-emanuele" è preso, prova: "siwrecipes-emanuele-2024"

### 3.9 Configura Variabili d'Ambiente

```powershell
# Database URL  
az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings DATABASE_URL="jdbc:postgresql://siwrecipes-db.postgres.database.azure.com:5432/siwrecipes?sslmode=require"

# Database credentials
az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings DB_USERNAME="siwadmin"

az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings DB_PASSWORD="your_strong_password"

# Google OAuth
az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings GOOGLE_CLIENT_ID="your_actual_google_client_id"

az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings GOOGLE_CLIENT_SECRET="your_actual_google_client_secret"

# Hibernate DDL
az webapp config appsettings set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --settings DDL_AUTO="update"
```

### 3.10 Deploy JAR

```powershell
az webapp deploy `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --src-path target\SiwRecipes-1.0.jar `
  --type jar
```

### 3.11 Configura Startup Command

```powershell
az webapp config set `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele `
  --startup-file "java -jar /home/site/wwwroot/SiwRecipes-1.0.jar"
```

### 3.12 Restart App

```powershell
az webapp restart `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

---

## 🌐 Passo 4: Aggiorna Google OAuth

**IMPORTANTE**: Aggiungi l'URL Azure a Google Console

1. Vai su: https://console.cloud.google.com
2. Credentials → OAuth Client ID
3. **Authorized redirect URIs** → Aggiungi:
   ```
   https://siwrecipes-emanuele.azurewebsites.net/login/oauth2/code/google
   ```
4. Salva

---

## ✅ Passo 5: Testa l'Applicazione

### Apri l'App

```powershell
az webapp browse `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

Oppure vai direttamente su:
```
https://siwrecipes-emanuele.azurewebsites.net
```

### Verifica Logs

```powershell
az webapp log tail `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

---

## 🔧 Comandi Utili

### Visualizza Info App
```powershell
az webapp show `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

### Restart App
```powershell
az webapp restart `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

### Stop App
```powershell
az webapp stop `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

### Start App
```powershell
az webapp start `
  --resource-group siwrecipes-rg `
  --name siwrecipes-emanuele
```

### Visualizza Costi
```powershell
az consumption usage list --output table
```

---

## 💰 Gestione Costi

### Costi Stimati (B1 tier)
- **App Service B1**: ~$13/mese
- **PostgreSQL B1ms**: ~$12/mese
- **Totale**: ~$25/mese

**Con crediti studenti**: Hai $100 per 12 mesi, quindi **4 mesi gratuiti**!

### Per Ridurre Costi

**Opzione 1: Free Tier** (durante sviluppo)
```powershell
# Cambia a Free tier (limitazioni: 60 min CPU/giorno)
az appservice plan update `
  --name siwrecipes-plan `
  --resource-group siwrecipes-rg `
  --sku FREE
```

**Opzione 2: Ferma quando non serve**
```powershell
az webapp stop --resource-group siwrecipes-rg --name siwrecipes-emanuele
```

---

## 🗑️ Cleanup (Elimina Tutto)

Quando hai finito:

```powershell
# Elimina tutto il resource group
az group delete --name siwrecipes-rg --yes --no-wait
```

---

## 📋 Checklist Deploy

- [ ] Account Azure Student creato ($100 crediti)
- [ ] Azure CLI installato
- [ ] Login Azure completato
- [ ] Resource Group creato
- [ ] PostgreSQL database creato
- [ ] App Service creato
- [ ] Variabili d'ambiente configurate
- [ ] JAR deployato
- [ ] Google OAuth redirect URI aggiornato
- [ ] App testata e funzionante

---

## 🐛 Troubleshooting

### App non si avvia
```powershell
# Controlla logs
az webapp log tail --resource-group siwrecipes-rg --name siwrecipes-emanuele
```

### Errore database
```powershell
# Verifica connessione
az postgres flexible-server show --resource-group siwrecipes-rg --name siwrecipes-db
```

### App lenta
- Tier B1 ha risorse limitate
- Considera upgrade a B2 o S1

---

## 🎓 Vantaggi Azure Student

✅ **$100 crediti gratuiti** (12 mesi)  
✅ **Servizi gratuiti** anche dopo crediti  
✅ **Certificazioni Microsoft** gratuite  
✅ **Learning paths** inclusi  
✅ **Ottimo per CV**  

---

**Inizia con la registrazione Azure Student e poi procediamo con il deploy!** 🚀
