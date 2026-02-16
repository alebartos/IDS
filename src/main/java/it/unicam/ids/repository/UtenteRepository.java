package it.unicam.ids.repository;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repository per la gestione degli Utenti.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class UtenteRepository {

    private final Map<Long, Utente> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Utente save(Utente utente) {
        if (utente.getId() == null) {
            utente.setId(idGenerator.getAndIncrement());
        }
        storage.put(utente.getId(), utente);
        return utente;
    }

    public Optional<Utente> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<Utente> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                .findFirst();
    }

    public List<Utente> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Utente> findByRuolo(Ruolo ruolo) {
        return storage.values().stream()
                .filter(u -> u.getRuoli().contains(ruolo))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public void deleteAll() {
        storage.clear();
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public boolean existsByEmail(String email) {
        return storage.values().stream()
                .anyMatch(u -> u.getEmail() != null && u.getEmail().equals(email));
    }

    public long count() {
        return storage.size();
    }
}
