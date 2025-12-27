# Οδηγίες Εγκατάστασης & Εκτέλεσης (Team Setup)

Αυτό το project αποτελείται από δύο μέρη:

1. **Backend:** Spring Boot (Port 8081)
2. **Frontend:** Next.js (Port 3000)

---

## Προαπαιτούμενα

Βεβαιωθείτε ότι έχετε εγκατεστημένα:

- **Java 17+** & Maven
- **Node.js** (Latest LTS)
- **Docker** (PostgreSQL container running)

---

## Πώς να τρέξετε την εφαρμογή

### 1. Βάση Δεδομένων

Βεβαιωθείτε ότι το Docker τρέχει:

```bash
docker ps
# Πρέπει να βλέπετε το container της postgres
```

2. Backend (Spring Boot)
   Ανοίξτε τον φάκελο spring-app στο IntelliJ (ή VS Code).

Κάντε Reload το Maven αν ζητηθεί.

Τρέξτε το αρχείο SpringAppApplication.

Προσοχή: Ο server πρέπει να ακούει στην πόρτα 8081.

3. Frontend (Next.js)
   Ανοίξτε τερματικό (Terminal) στον κεντρικό φάκελο του project.

Μπείτε στον φάκελο του frontend:

Bash

cd frontend
Εγκαταστήστε τα dependencies (Μόνο την 1η φορά):

Bash

npm install
Ξεκινήστε τον server ανάπτυξης:

Bash

npm run dev
Ανοίξτε τον browser στο: http://localhost:3000

Λειτουργίες που υλοποιήθηκαν
Δημιουργία Συνταγής: Πλήρης φόρμα με βήματα, υλικά βήματος και φωτογραφίες (URL).

Validation: Έλεγχος ώστε ο Συνολικός Χρόνος να ταιριάζει με το άθροισμα των βημάτων.

Μαγείρεμα: Διαδραστική μπάρα προόδου.

Edit: Δυνατότητα επεξεργασίας υπαρχουσών συνταγών.

### Βήμα 3: Ανέβασμα στο GitHub

Τώρα πρέπει να στείλεις αυτό το νέο αρχείο στο repo.

Άνοιξε το τερματικό στο VS Code και γράψε:

```bash
git add INSTRUCTIONS.md
git commit -m "Add separate instructions file for the team"
git push origin staging
```
