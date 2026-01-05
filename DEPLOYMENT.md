# SiwRecipes - Deploy & OAuth Guide

## 🚀 Deployment su Heroku

### Requisiti
- Account Heroku (gratuito): https://signup.heroku.com
- Heroku CLI installato: https://devcenter.heroku.com/articles/heroku-cli
- Git installato

### Step-by-Step

```bash
# 1. Login a Heroku
heroku login

# 2. Crea l'applicazione
heroku create siwrecipes-[your-name]

# 3. Aggiungi PostgreSQL
heroku addons:create heroku-postgresql:essential-0

# 4. Configura le variabili d'ambiente (vedi sezione OAuth)
heroku config:set GOOGLE_CLIENT_ID=your_client_id_here
heroku config:set GOOGLE_CLIENT_SECRET=your_client_secret_here
heroku config:set DDL_AUTO=update

# 5. Inizializza Git (se non già fatto)
git init
git add .
git commit -m "Initial commit - SiwRecipes"

# 6. Deploy
git push heroku main
# (o git push heroku master se il branch è master)

# 7. Apri l'applicazione
heroku open

# 8. Visualizza i log
heroku logs --tail
```

---

## 🔐 Configurazione Google OAuth2

### 1. Google Cloud Console

1. Vai su: https://console.cloud.google.com
2. Crea un nuovo progetto: "SiwRecipes"
3. Abilita "Google+ API" (o "Google Identity")

### 2. Crea OAuth Client ID

1. Vai su: **API & Services → Credentials**
2. Click su **Create Credentials → OAuth 2.0 Client ID**
3. Application type: **Web application**
4. Nome: "SiwRecipes Web Client"

### 3. Authorized redirect URIs

Aggiungi questi URI:

**Locale**:
- `http://localhost:8080/login/oauth2/code/google`

**Produzione** (sostituisci con il tuo URL Heroku):
- `https://siwrecipes-[your-name].herokuapp.com/login/oauth2/code/google`

### 4. Copia le credenziali

- **Client ID**: `xxxxx.apps.googleusercontent.com`
- **Client Secret**: `GOCSPX-xxxxxxx`

### 5. Configura l'applicazione

**Locale** (crea file `.env` o setta variabili):
```properties
GOOGLE_CLIENT_ID=your_client_id_here
GOOGLE_CLIENT_SECRET=your_client_secret_here
```

**Heroku**:
```bash
heroku config:set GOOGLE_CLIENT_ID=your_client_id_here
heroku config:set GOOGLE_CLIENT_SECRET=your_client_secret_here
```

---

## 🐙 Configurazione GitHub OAuth (Opzionale)

### 1. GitHub Settings

1. Vai su: https://github.com/settings/developers
2. Click su **New OAuth App**

### 2. Form di registrazione

- **Application name**: SiwRecipes
- **Homepage URL**: 
  - Locale: `http://localhost:8080`
  - Prod: `https://siwrecipes-[your-name].herokuapp.com`
- **Authorization callback URL**:
  - Locale: `http://localhost:8080/login/oauth2/code/github`
  - Prod: `https://siwrecipes-[your-name].herokuapp.com/login/oauth2/code/github`

### 3. Copia le credenziali

- **Client ID**: Visualizzato subito
- **Client Secret**: Click su "Generate a new client secret"

### 4. Configura

```bash
heroku config:set GITHUB_CLIENT_ID=your_github_client_id
heroku config:set GITHUB_CLIENT_SECRET=your_github_client_secret
```

---

## 🧪 Test in Locale

### 1. Setta le variabili d'ambiente

**Windows (PowerShell)**:
```powershell
$env:GOOGLE_CLIENT_ID="your_client_id"
$env:GOOGLE_CLIENT_SECRET="your_client_secret"
```

**Windows (CMD)**:
```cmd
set GOOGLE_CLIENT_ID=your_client_id
set GOOGLE_CLIENT_SECRET=your_client_secret
```

###2. Avvia l'applicazione

```bash
./mvnw spring-boot:run
```

### 3. Testa OAuth

1. Vai su: http://localhost:8080/login
2. Click su "Accedi con Google"
3. Seleziona il tuo account Google
4. Dovresti essere reindirizzato a `/recipes`

---

## 📋 Checklist Pre-Deploy

- [ ] Google OAuth configurato e testato localmente
- [ ] Redirect URIs configurati per produzione
- [ ] File `Procfile` presente
- [ ] File `system.properties` presente
- [ ] `.gitignore` aggiornato (non committare `.env`)
- [ ] Build Maven funzionante (`./mvnw clean package`)
- [ ] Heroku CLI installato
- [ ] Variabili d'ambiente configurate su Heroku

---

## 🔧 Comandi Utili Heroku

```bash
# Verifica configurazione
heroku config

# Restart app
heroku restart

# Visualizza logs
heroku logs --tail

# Accedi al database
heroku pg:psql

# Info app
heroku apps:info

# Scale dynos
heroku ps:scale web=1
```

---

## ⚠️ Troubleshooting

### Errore: "Application error"
```bash
heroku logs --tail
# Controlla i log per errori specifici
```

### Errore OAuth: "redirect_uri_mismatch"
- Verifica che i redirect URI in Google Console matchino esattamente
- Assicurati di usare `https://` per produzione

### Database non funziona
```bash
# Controlla la connessione
heroku config | grep DATABASE_URL
```

### Build fallisce
```bash
# Verifica Java version
cat system.properties
# Deve essere: java.runtime.version=17
```

---

## 🎯 Struttura File Deployment

```
SiwRecipes/
├── Procfile                    # Heroku process file
├── system.properties           # Java version
├── .env.example               # Environment variables template
├── .gitignore                 # Git ignore (include .env)
├── pom.xml                    # Maven dependencies
└── src/
    └── main/
        ├── java/
        └── resources/
            └── application.properties  # Spring configuration
```

---

## 📖 Risorse

- [Heroku Java Support](https://devcenter.heroku.com/articles/java-support)
- [Google OAuth2 Setup](https://developers.google.com/identity/protocols/oauth2)
- [GitHub OAuth Apps](https://docs.github.com/en/developers/apps/building-oauth-apps)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)

---

## ✅ Dopo il Deploy

1. **Testa Login Locale**: Funziona con email/password
2. **Testa OAuth Locale**: Funziona con Google
3. **Deploy su Heroku**: Push del codice
4. **Testa Login Produzione**: Verifica funzionamento
5. **Monitora Logs**: Controlla per errori

**Applicazione pronta per la produzione!** 🎉
