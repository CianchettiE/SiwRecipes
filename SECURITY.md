# 🔒 Guida alla Sicurezza per GitHub

Questo documento spiega come configurare il progetto **SiwRecipes** in modo sicuro prima di caricarlo su GitHub.

---

## ✅ Cosa È Stato Fatto

Ho già rimosso tutte le credenziali hardcoded dai file del progetto:

- ✅ **GOOGLE_OAUTH_SETUP.md**: Credenziali sostituite con placeholder
- ✅ **AZURE_DEPLOY.md**: Credenziali sostituite con placeholder
- ✅ **start.ps1**: Script aggiornato per usare variabili d'ambiente
- ✅ **.gitignore**: Aggiornato per proteggere file con credenziali

---

## 🔐 Come Gestire le Credenziali in Modo Sicuro

### **1. NON committare MAI credenziali su Git**

Le credenziali includono:
- Google OAuth Client ID e Client Secret
- Password del database
- Qualsiasi API key o token

### **2. Usa Variabili d'Ambiente**

**Per sviluppo locale** (PowerShell):

```powershell
# Imposta le credenziali nella sessione corrente
$env:GOOGLE_CLIENT_ID="your_actual_client_id"
$env:GOOGLE_CLIENT_SECRET="your_actual_client_secret"

# Avvia l'app
./mvnw spring-boot:run
```

**Per deployment su Azure/Heroku**:
- Usa le configurazioni delle piattaforme per impostare le variabili d'ambiente
- Mai includere credenziali nei file di configurazione committati

### **3. File da Proteggere**

Il file `.gitignore` è configurato per ignorare:
- `.env` e varianti
- `start-with-credentials.ps1` (se crei script personali con credenziali)
- `credentials/` (qualsiasi cartella con questo nome)
- File di backup (`*.bak`, `*.orig`)

---

## 📋 Checklist Pre-Commit

Prima di fare `git push`, verifica:

- [ ] **Nessuna credenziale** in file `.md`, `.properties`, `.yml`
- [ ] **`.gitignore` aggiornato** (già fatto ✅)
- [ ] **File di configurazione** usano `${VARIABILE_AMBIENTE}` o placeholder
- [ ] **Test locale** funziona con variabili d'ambiente
- [ ] **Nessun file sensibile** nella staging area

### Comando Rapido di Verifica

```powershell
# Cerca credenziali hardcoded (controlla che non ci siano risultati)
git grep -i "GOCSPX-"
git grep -i "client.secret"
```

Se questi comandi **non** trovano nulla, sei al sicuro! ✅

---

## 🚀 Configurazione per Nuovi Collaboratori

Quando qualcuno clona il repository, deve:

1. **Ottenere le proprie credenziali Google OAuth** seguendo `GOOGLE_OAUTH_SETUP.md`
2. **Impostare le variabili d'ambiente**:
   ```powershell
   $env:GOOGLE_CLIENT_ID="loro_client_id"
   $env:GOOGLE_CLIENT_SECRET="loro_client_secret"
   ```
3. **Avviare l'applicazione**: `./mvnw spring-boot:run`

---

## 🔧 Strumenti Opzionali

### Script di Setup Locale (NON committare!)

Puoi creare un file `start-with-credentials.ps1` (già in `.gitignore`) per uso personale:

```powershell
# ⚠️ QUESTO FILE NON DEVE ESSERE COMMITTATO!
$env:GOOGLE_CLIENT_ID="tuo_client_id_qui"
$env:GOOGLE_CLIENT_SECRET="tuo_client_secret_qui"
./mvnw spring-boot:run
```

### File .env (NON committare!)

Oppure crea un file `.env` (già in `.gitignore`):

```
GOOGLE_CLIENT_ID=your_actual_client_id
GOOGLE_CLIENT_SECRET=your_actual_client_secret
```

E caricalo manualmente quando serve.

---

## ⚠️ Cosa Fare se Hai Committato Credenziali per Errore

Se hai già pushato credenziali su GitHub:

1. **Revoca IMMEDIATAMENTE** le credenziali su Google Cloud Console:
   - Vai su https://console.cloud.google.com
   - Credentials → Elimina il Client ID compromesso
   - Crea un nuovo Client ID

2. **Rimuovi dal Git History** (avanzato):
   ```powershell
   git filter-branch --force --index-filter \
     "git rm --cached --ignore-unmatch NOME_FILE" \
     --prune-empty --tag-name-filter cat -- --all
   ```

3. **Force push**:
   ```powershell
   git push origin --force --all
   ```

4. **Informa il team** del cambio di credenziali

---

## 🎯 Best Practices

✅ **Usa `.gitignore` correttamente**  
✅ **Variabili d'ambiente per credenziali**  
✅ **Placeholder nei file di esempio**  
✅ **Documentazione chiara** su come configurare credenziali  
✅ **Review del codice** prima del commit  
✅ **Git hooks** per validazione (opzionale)  

❌ **Mai hardcodare credenziali**  
❌ **Mai committare file `.env`**  
❌ **Mai condividere credenziali via chat/email**  
❌ **Mai usare credenziali di produzione in development**  

---

## 📚 Risorse Aggiuntive

- [GitHub: Removing sensitive data](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/removing-sensitive-data-from-a-repository)
- [12-Factor App: Config](https://12factor.net/config)
- [OWASP: Credential Management](https://owasp.org/www-community/vulnerabilities/Use_of_hard-coded_password)

---

## ✅ Stato Attuale

Tutti i file del progetto **SiwRecipes** sono ora sicuri per il commit su GitHub! 🎉

Le credenziali sono gestite tramite variabili d'ambiente e nessuna informazione sensibile è presente nei file tracciati da Git.
