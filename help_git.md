# Βασικές Εντολές Git για το Project

Ακολουθούν οι κύριες εντολές git που θα χρειαστείτε για τη διαχείριση του κώδικα του project:

## 1. Δημιουργία νέου branch από το main

```bash
    git checkout main
    git pull origin main
    git checkout -b <όνομα-νέου-branch>
```

## 2. Έλεγχος κατάστασης αρχείων (status)

```bash
  git status
```

## 3. Συγχώνευση αλλαγών από το main στο τρέχον branch

```bash
  git merge origin/main
```

## 4. Λήψη των τελευταίων αλλαγών από το απομακρυσμένο repository

```bash
  git pull
```

## 5. Αποστολή των αλλαγών σας στο απομακρυσμένο repository

```bash
  git push
```

---

### Συμβουλές
- Πριν ξεκινήσετε νέα εργασία, βεβαιωθείτε ότι το branch σας είναι ενημερωμένο με το main (`git pull origin main`).
- Κάνετε συχνά commit και push για να μην χάσετε τη δουλειά σας.
- Χρησιμοποιείτε περιγραφικά ονόματα για τα branches σας.

