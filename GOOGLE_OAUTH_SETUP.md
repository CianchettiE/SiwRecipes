# Google OAuth2 Setup - Guida Step-by-Step

## 📋 Passi da Seguire

### 1️⃣ Crea Progetto Google Cloud

1. Vai su: https://console.cloud.google.com
2. Click sul selettore progetti (in alto a sinistra)
3. Click su **"New Project"** / **"Nuovo Progetto"**
4. Nome progetto: `SiwRecipes`
5. Click **"Create"** / **"Crea"**

---

### 2️⃣ Configura OAuth Consent Screen

1. Nel menu laterale, vai su: **APIs & Services** → **OAuth consent screen**
2. Scegli tipo: **External** (per test puoi usare questo)
3. Click **"Create"** / **"Crea"**

**Informazioni app**:
- App name: `SiwRecipes`
- User support email: [tua email]
- Developer contact: [tua email]

4. Click **"Save and Continue"**

**Scopes**: 
- Click **"Add or Remove Scopes"**
- Seleziona:
  - `.../auth/userinfo.email`
  - `.../auth/userinfo.profile`
- Click **"Update"** e poi **"Save and Continue"**

**Test users** (opzionale per development):
- Aggiungi la tua email come test user
- Click **"Save and Continue"**

5. Review e click **"Back to Dashboard"**

---

### 3️⃣ Crea OAuth 2.0 Client ID

1. Nel menu laterale: **APIs & Services** → **Credentials**
2. Click **"+ CREATE CREDENTIALS"** → **"OAuth client ID"**

**Configurazione**:
- Application type: **Web application**
- Name: `SiwRecipes Web Client`

**Authorized JavaScript origins** (opzionale):
- `http://localhost:8080`

**Authorized redirect URIs** (IMPORTANTE!):
- `http://localhost:8080/login/oauth2/code/google`

> ⚠️ **Importante**: L'URI deve essere ESATTAMENTE questo!

3. Click **"Create"** / **"Crea"**

---

### 4️⃣ Copia le Credenziali

Apparirà un popup con:
- **Client ID**: `xxxxx-xxxxxx.apps.googleusercontent.com`
- **Client secret**: `GOCSPX-xxxxxxxxxxxxx`

📋 **COPIA ENTRAMBI** e salvali temporaneamente (NON in questo file!):

```
GOOGLE_CLIENT_ID=your_actual_client_id_here
GOOGLE_CLIENT_SECRET=your_actual_client_secret_here
```

> ⚠️ **NON SCRIVERE** le credenziali reali in questo file! Verranno configurate tramite variabili d'ambiente.


---

### 5️⃣ Configura l'Applicazione

**Metodo 1: File .env (consigliato per sviluppo)**

Crea file `.env` nella root del progetto:

```properties
GOOGLE_CLIENT_ID=your_client_id_here
GOOGLE_CLIENT_SECRET=your_client_secret_here
```

**Metodo 2: Variabili d'ambiente Windows (PowerShell)**

```powershell
$env:GOOGLE_CLIENT_ID="your_client_id_here"
$env:GOOGLE_CLIENT_SECRET="your_client_secret_here"
```

**Metodo 3: Variabili d'ambiente Windows (CMD)**

```cmd
set GOOGLE_CLIENT_ID=your_client_id_here
set GOOGLE_CLIENT_SECRET=your_client_secret_here
```

---

### 6️⃣ Testa l'Applicazione

1. Avvia l'applicazione:
```bash
./mvnw spring-boot:run
```

2. Apri browser: http://localhost:8080/login

3. Click su **"Accedi con Google"**

4. Seleziona il tuo account Google

5. Dovresti essere reindirizzato a `/recipes` ✅

---

## 🔧 Troubleshooting

### Errore: "redirect_uri_mismatch"

**Causa**: L'URI di redirect non corrisponde

**Soluzione**:
1. Torna su Google Cloud Console → Credentials
2. Click sul tuo OAuth client
3. Verifica che in "Authorized redirect URIs" ci sia ESATTAMENTE:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
4. Salva e riprova

### Errore: "Access blocked: This app's request is invalid"

**Causa**: OAuth consent screen non configurato

**Soluzione**: Completa la configurazione dell'OAuth consent screen (Step 2)

### Errore: "Error 400: invalid_request"

**Causa**: Credenziali non configurate o errate

**Soluzione**: Verifica che le variabili d'ambiente siano state impostate correttamente

---

## 📝 Note Importanti

- ✅ Le credenziali Google sono **GRATUITE**
- ✅ Durante sviluppo, l'app sarà in "Testing" mode
- ✅ Per pubblicare, dovrai verificare l'app con Google (non necessario per sviluppo)
- ⚠️ **NON committare** le credenziali su Git!
- ⚠️ Il file `.env` è già in `.gitignore`

---

