package it.unicam.ids.repository;

import it.unicam.ids.model.Hackathon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class HackathonRepository {

    private final Map<Long, Hackathon> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Hackathon add(Hackathon hackathon) {
        if (hackathon.getId() == null) {
            hackathon.setId(idGenerator.getAndIncrement());
        }
        storage.put(hackathon.getId(), hackathon);
        return hackathon;
    }

    public void modifyRecord(Hackathon hackathon) {
        storage.put(hackathon.getId(), hackathon);
    }

    public Optional<Hackathon> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Hackathon> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Hackathon> getAllHackathon() {
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

    public Optional<Hackathon> findByName(String nome) {
        return storage.values().stream()
                .filter(hackathon -> hackathon.getNome() != null && hackathon.getNome().equals(nome))
                .findFirst();
    }

    public Optional<Hackathon> findByIdOrganizzatore(Long organizzatoreId) {
        return storage.values().stream()
                .filter(hackathon -> hackathon.getOrganizzatoreId() != null
                        && hackathon.getOrganizzatoreId().equals(organizzatoreId))
                .findFirst();
    }
}
