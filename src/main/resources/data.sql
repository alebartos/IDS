-- Dati iniziali per il sistema IDS Hackathon
-- Questo file viene eseguito all'avvio dell'applicazione.
-- Usa MERGE per evitare duplicati nei riavvii.

-- 1. Organizzatore (id=1)
MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (1, 'Admin', 'Organizzatore', 'organizzatore@ids.com', 'password123');

-- 2. Membro Staff (id=2) - può essere assegnato come Giudice
MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (2, 'Marco', 'Staff', 'staff@ids.com', 'password123');

-- 3-7. Utenti base per creare team, inviti, ecc.
MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (3, 'Mario', 'Rossi', 'mario.rossi@ids.com', 'password123');

MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (4, 'Luigi', 'Bianchi', 'luigi.bianchi@ids.com', 'password123');

MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (5, 'Anna', 'Verdi', 'anna.verdi@ids.com', 'password123');

MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (6, 'Sara', 'Neri', 'sara.neri@ids.com', 'password123');

MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (7, 'Paolo', 'Gialli', 'paolo.gialli@ids.com', 'password123');

-- 8. Utente mentore (può essere assegnato come Mentore)
MERGE INTO utente (id, nome, cognome, email, password) KEY(id)
    VALUES (8, 'Luca', 'Mentore', 'mentore@ids.com', 'password123');

-- Ruoli: cancella e reinserisci per ogni utente pre-configurato
-- (evita duplicati su riavvii)

DELETE FROM utente_ruoli WHERE utente_id IN (1, 2);

-- Ruoli Organizzatore
INSERT INTO utente_ruoli (utente_id, ruoli) VALUES (1, 'BASE');
INSERT INTO utente_ruoli (utente_id, ruoli) VALUES (1, 'ORGANIZZATORE');
INSERT INTO utente_ruoli (utente_id, ruoli) VALUES (1, 'MEMBRO_STAFF');

-- Ruoli Membro Staff
INSERT INTO utente_ruoli (utente_id, ruoli) VALUES (2, 'BASE');
INSERT INTO utente_ruoli (utente_id, ruoli) VALUES (2, 'MEMBRO_STAFF');
