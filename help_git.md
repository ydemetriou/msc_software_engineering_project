# Ξεκινώντας με το Git

## 1. Δημιουργία SSH Key (αν δεν έχετε ήδη)
Ακολουθήστε τον επίσημο οδηγό της GitHub για να δημιουργήσετε νέο SSH key:
[Δημιουργία SSH Key](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

## 2. Προσθήκη του SSH Key στο GitHub
Οδηγίες για να προσθέσετε το SSH key στον λογαριασμό σας στο GitHub:
[Προσθήκη SSH Key στο GitHub](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account)

## 3. Κλωνοποίηση του repository με SSH
Χρησιμοποιήστε το παρακάτω command για να κάνετε clone το project:
```bash
  git clone git@github.com:ydemetriou/msc_software_engineering_project.git
```

---

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
