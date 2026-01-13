# Οδηγός εκτέλεσης του start σε κάθε σύστημα

Αυτό το έγγραφο εξηγεί πώς να τρέξετε το script εκκίνησης του stack με Docker Compose, σε Linux/macOS και Windows.

Προαπαιτούμενα:
- Εγκατεστημένο Docker (Linux: Docker Engine, macOS/Windows: Docker Desktop) και διαθέσιμο στο PATH.
- Πρόσβαση του Docker στο δίσκο/φάκελο του project (σε Windows/Mac: File Sharing για τη μονάδα δίσκου).

## Linux / macOS (bash/sh)

Βήματα:
1) (Αν χρειάζεται) δώστε δικαίωμα εκτέλεσης στο script μία φορά:
```bash
  chmod +x ./start
```

2) Τρέξτε το script από τη ρίζα του project:
```bash
  ./start
```

Τι συμβαίνει:
- Ζητά πόρτες για Spring Boot (προεπιλογή 8081), MySQL (3306), phpMyAdmin (8082).
- Δημιουργεί/ενημερώνει `.env` στη ρίζα του project (δίπλα στο `start`).
- Εκτελεί `docker-compose up --build` ή, αν δεν υπάρχει, `docker compose up --build`.

## Windows (Cmd)

Βήματα:
1) Άνοιξε Command Prompt (Cmd) στη ρίζα του project.

2) Τρέξε:
```bat
    start.cmd
```

Τι συμβαίνει:
- Ζητά πόρτες (Spring Boot, MySQL, phpMyAdmin).
- Δημιουργεί/ενημερώνει `.env` στον ίδιο φάκελο με το `start.cmd`.
- Εκτελεί `docker-compose up --build` ή, αν δεν υπάρχει, `docker compose up --build`.

## Συχνά θέματα
- Docker δεν βρίσκεται στο PATH: άνοιξε/επανεκκίνησε Docker Desktop (Mac/Windows) ή εγκατάστησε Docker Engine (Linux).
- Άρνηση πρόσβασης σε δίσκο (Windows/Mac): ενεργοποίησε File Sharing για τη μονάδα δίσκου που περιέχει το project.
- Πόρτες σε χρήση: διάλεξε άλλες τιμές όταν ρωτηθείς (π.χ. 8083 για Spring, 3307 για MySQL).

## Γρήγορη επαλήθευση
- Έλεγξε ότι δημιουργήθηκε το `.env` στη ρίζα του project (Linux/macOS) ή δίπλα στο `start.cmd` (Windows).
- Δες τα logs του Docker Compose στο τερματικό. Αν όλα είναι σωστά, θα ξεκινήσουν τα services (Spring app, MySQL, phpMyAdmin).
