# Προαπαιτούμενο: Εγκατάσταση Docker

Για να εκτελέσετε το project, απαιτείται να έχετε εγκατεστημένο το Docker στη μηχανή σας.

## Εγκατάσταση Docker

### Linux (π.χ. Ubuntu)
Ανοίξτε τερματικό και εκτελέστε:
```bash
sudo apt update
sudo apt install docker.io docker-compose -y
sudo systemctl enable --now docker
```

### Windows
1. Κατεβάστε και εγκαταστήστε το [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/).
2. Επανεκκινήστε τον υπολογιστή αν σας ζητηθεί.
3. Βεβαιωθείτε ότι το Docker Desktop τρέχει πριν συνεχίσετε με τα βήματα του project.

---

# Οδηγίες Εκκίνησης και Διαχείρισης του Project με Docker

Ακολουθήστε τα παρακάτω βήματα για να εκκινήσετε και να διαχειριστείτε το project για πρώτη φορά:

## 1. Δημιουργία αρχείου περιβάλλοντος (.env)

Αντιγράψτε το αρχείο `.env.example` σε `.env`:
```bash
  cp .env.example .env
```
Προσαρμόστε τις μεταβλητές περιβάλλοντος στο `.env` αν χρειάζεται.

## 2. Εκκίνηση του project για πρώτη φορά

Από το root folder του project (όπου βρίσκεται το `docker-compose.yml`):
```bash
  docker-compose up --build
```
Το `--build` είναι απαραίτητο την πρώτη φορά ή όταν αλλάξετε το Dockerfile.

## 3. Επανεκκίνηση containers (αν χρειαστεί)
Για επανεκκίνηση όλων των containers:
```bash
  docker-compose restart
```
Για επανεκκίνηση συγκεκριμένου container (π.χ. spring-app):
```bash
  docker-compose restart spring-app
```

## 4. Εκκίνηση/Διακοπή/Έλεγχος containers
- Εκκίνηση (χωρίς build):
```bash
  docker-compose up
```
- Εκκίνηση στο background:
```bash
  docker-compose up -d
```
- Έλεγχος τρεχόντων containers και health status:
```bash
  docker-compose ps
```
- Σταμάτημα και αφαίρεση containers (κρατά volumes):
```bash
  docker-compose down
```
- Σταμάτημα και διαγραφή volumes:
```bash
  docker-compose down -v
```

## 5. Πρόσβαση σε υπηρεσίες
- Spring Boot app: [http://localhost:8080](http://localhost:8080) (port από `.env`)
- phpMyAdmin: [http://localhost:8082](http://localhost:8082)
  - Χρήστης: root, Κωδικός: root

## 6. Πρόσβαση σε containers
- Είσοδος σε MySQL container:
```bash
  docker exec -it mysql-db bash
```
- Είσοδος σε Spring Boot container:
```bash
  docker-compose exec spring-app bash
```

## 7. Προβολή logs
- Όλων των containers:
```bash
  docker-compose logs
```
- Συγκεκριμένου container (π.χ. spring-app):
```bash
  docker-compose logs spring-app
```
- Live παρακολούθηση logs:
```bash
  docker-compose logs -f spring-app
```

## 8. Προβολή/Έλεγχος μεταβλητών περιβάλλοντος container
```bash
  docker-compose exec spring-app printenv
```

## 9. Διαγραφή αχρησιμοποίητων images, containers, networks, cache
```bash
  docker system prune
```

## 10. Αντιγραφή αρχείων από/προς container
```bash
  docker cp spring-app:/path/in/container /path/on/host
```

---