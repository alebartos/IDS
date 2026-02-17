package it.unicam.ids.repository;

import it.unicam.ids.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TeamRepository {

    private final Map<Long, Team> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Team add(Team team) {
        if (team.getId() == null) {
            team.setId(idGenerator.getAndIncrement());
        }
        storage.put(team.getId(), team);
        return team;
    }

    public void modifyRecord(Team team) {
        storage.put(team.getId(), team);
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<Team> findByName(String nome) {
        return storage.values().stream()
                .filter(team -> team.getNome() != null && team.getNome().equals(nome))
                .findFirst();
    }

    public Optional<Team> findByUtenteId(Long utenteId) {
        return storage.values().stream()
                .filter(team -> team.getMembri().contains(utenteId) ||
                        (team.getLeaderId() != null && team.getLeaderId().equals(utenteId)))
                .findFirst();
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

    public Optional<Team> findByLeaderId(Long leaderId) {
        return storage.values().stream()
                .filter(team -> team.getLeaderId() != null && team.getLeaderId().equals(leaderId))
                .findFirst();
    }
}
