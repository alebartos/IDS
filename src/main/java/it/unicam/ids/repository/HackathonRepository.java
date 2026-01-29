package it.unicam.ids.repository;

import it.unicam.ids.model.Hackathon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository per la gestione degli Hackathon.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class HackathonRepository {

    private final Map<Long, Hackathon> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Hackathon save(Hackathon hackathon) {
        if (hackathon.getId() == null) {
            hackathon.setId(idGenerator.getAndIncrement());
        }
        storage.put(hackathon.getId(), hackathon);
        return hackathon;
    }

    public Optional<Hackathon> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Hackathon> findAll() {
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

    public boolean existsByNome(String nome) {
        return storage.values().stream()
                .anyMatch(hackathon -> hackathon.getNome() != null && hackathon.getNome().equals(nome));
    }
}
