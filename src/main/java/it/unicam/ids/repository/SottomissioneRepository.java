package it.unicam.ids.repository;

import it.unicam.ids.model.Sottomissione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SottomissioneRepository {

    private final Map<Long, Sottomissione> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Sottomissione add(Sottomissione sottomissione) {
        if (sottomissione.getId() == null) {
            sottomissione.setId(idGenerator.getAndIncrement());
        }
        storage.put(sottomissione.getId(), sottomissione);
        return sottomissione;
    }

    public void modifyRecord(Sottomissione sottomissione) {
        storage.put(sottomissione.getId(), sottomissione);
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

    public Optional<Sottomissione> findByTeamAndHackathon(Long teamId, Long hackathonId) {
        return storage.values().stream()
                .filter(s -> s.getTeamId() != null &&
                        s.getTeamId().equals(teamId) &&
                        s.getHackathonId() != null &&
                        s.getHackathonId().equals(hackathonId))
                .findFirst();
    }

    public List<Sottomissione> findByTeamId(Long teamId) {
        return storage.values().stream()
                .filter(s -> s.getTeamId() != null && s.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    public List<Sottomissione> findByHackathonId(Long hackathonId) {
        return storage.values().stream()
                .filter(s -> s.getHackathonId() != null && s.getHackathonId().equals(hackathonId))
                .collect(Collectors.toList());
    }
}
