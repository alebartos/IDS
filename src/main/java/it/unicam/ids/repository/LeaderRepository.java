package it.unicam.ids.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository per la gestione dei Leader.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class LeaderRepository {

    private final Map<Long, Leader> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Leader save(Leader leader) {
        if (leader.getId() == null) {
            leader.setId(idGenerator.getAndIncrement());
        }
        storage.put(leader.getId(), leader);
        return leader;
    }

    public Optional<Leader> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Leader> findAll() {
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
