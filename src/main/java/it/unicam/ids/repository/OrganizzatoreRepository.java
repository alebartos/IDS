package it.unicam.ids.repository;

import it.unicam.ids.model.Organizzatore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository per la gestione degli Organizzatori.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class OrganizzatoreRepository {

    private final Map<Long, Organizzatore> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Organizzatore save(Organizzatore organizzatore) {
        if (organizzatore.getId() == null) {
            organizzatore.setId(idGenerator.getAndIncrement());
        }
        storage.put(organizzatore.getId(), organizzatore);
        return organizzatore;
    }

    public Optional<Organizzatore> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Organizzatore> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteAll() {
        storage.clear();
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public long count() {
        return storage.size();
    }
}
