package it.unicam.ids.repository;

import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repository per la gestione dei Team.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class TeamRepository {

    private final Map<Long, Team> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Team save(Team team) {
        if (team.getId() == null) {
            team.setId(idGenerator.getAndIncrement());
        }
        storage.put(team.getId(), team);
        return team;
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Team> findAll() {
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
                .anyMatch(team -> team.getNome() != null && team.getNome().equals(nome));
    }

    public Optional<Team> findByLeader(Utente leader) {
        return storage.values().stream()
                .filter(team -> team.getLeader() != null && team.getLeader().equals(leader))
                .findFirst();
    }

    public Optional<Team> findByLeaderId(Long leaderId) {
        return storage.values().stream()
                .filter(team -> team.getLeader() != null && team.getLeader().getId().equals(leaderId))
                .findFirst();
    }
}
