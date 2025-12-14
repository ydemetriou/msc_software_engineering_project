# Οδηγία για αρχείο περιβάλλοντος (.env)

Για να λειτουργήσει σωστά το project, πρέπει να αντιγράψετε το αρχείο `.env.example` σε `.env` πριν εκκινήσετε τα containers:

```bash
cp .env.example .env
```

Στη συνέχεια, μπορείτε να προσαρμόσετε τις μεταβλητές περιβάλλοντος στο `.env` ανάλογα με τις ανάγκες σας.

# Docker Commands Cheat Sheet for Cooking Recipe Project

Αυτό το αρχείο περιέχει τις βασικές εντολές για να τρέξει το project με Docker για πρώτη φορά, καθώς και για να έχεις πρόσβαση στον MySQL και στον Spring Boot container.

---

## Εκκίνηση project για πρώτη φορά

Από το root folder του project (όπου βρίσκεται το `docker-compose.yml`):

Κατεβάζει τα images, κάνει build το Spring Boot app και τρέχει όλα τα containers
```bash
  docker-compose up --build
```

#### Το --build είναι απαραίτητο την πρώτη φορά ή όταν αλλάξεις το Dockerfile στο project.


## Μετά το πρώτο build, μπορείς απλά να τρέχεις:
```bash
  docker-compose up
```

### Για να τρέξει στο background:
```bash
  docker-compose up -d
````

### Έλεγχος τρεχόντων containers
```bash
  docker-compose ps
 ```
#### Δείχνει τα containers που ανήκουν στο τρέχον Docker Compose project.

## Πρόσβαση στον MySQL container
```bash
  docker exec -it mysql-db bash
```

#### Συνδέεται απευθείας στον MySQL container που τρέχει ήδη.

Password: root

## Πρόσβαση στην εφαρμογή Spring Boot

Η εφαρμογή είναι διαθέσιμη στο:
http://localhost:8080
(Το port ορίζεται στο αρχείο `.env` με τη μεταβλητή SPRING_SERVER_PORT)

## Πρόσβαση στο phpMyAdmin

Το phpMyAdmin είναι διαθέσιμο στο:
http://localhost:8082

Χρησιμοποιήστε τα ίδια στοιχεία σύνδεσης με τη βάση δεδομένων MySQL (user: root, password: root).

Το phpMyAdmin προστέθηκε για εύκολη διαχείριση της βάσης δεδομένων μέσω web περιβάλλοντος.

## Πρόσβαση στον Spring Boot container
Δες τα logs της εφαρμογής
```bash
  docker-compose logs spring-app
```

## Σβήσιμο containers / network
### Σταματάει και αφαιρεί containers αλλά κρατά volumes
```bash
  docker-compose down
```

### Σβήνει και volumes
```bash
  docker-compose down -v
```