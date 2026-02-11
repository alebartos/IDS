package it.unicam.ids.repository;

import it.unicam.ids.model.Sottomissione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repository per la gestione delle Sottomissioni.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class SottomissioneRepository {

    private final Map<Long, Sottomissione> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Sottomissione save(Sottomissione sottomissione) {
        if (sottomissione.getId() == null) {
            sottomissione.setId(idGenerator.getAndIncrement());
        }
        storage.put(sottomissione.getId(), sottomissione);
        return sottomissione;
    }

    public Optional<Sottomissione> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Sottomissione> findAll() {
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

    /**
     * Trova una sottomissione per team e hackathon.
     */
    public Optional<Sottomissione> findByTeamAndHackathon(Long teamId, Long hackathonId) {
        return storage.values().stream()
                .filter(s -> s.getTeamId() != null &&
                        s.getTeamId().equals(teamId) &&
                        s.getHackathonId() != null &&
                        s.getHackathonId().equals(hackathonId))
                .findFirst();
    }

    /**
     * Trova tutte le sottomissioni di un team.
     */
    public List<Sottomissione> findByTeamId(Long teamId) {
        return storage.values().stream()
                .filter(s -> s.getTeamId() != null && s.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    /**
     * Trova tutte le sottomissioni per un hackathon.
     */
    public List<Sottomissione> findByHackathonId(Long hackathonId) {
        return storage.values().stream()
                .filter(s -> s.getHackathonId() != null && s.getHackathonId().equals(hackathonId))
                .collect(Collectors.toList());
    }
}
