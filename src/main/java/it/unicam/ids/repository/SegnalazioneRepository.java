package it.unicam.ids.repository;

import it.unicam.ids.model.Segnalazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class SegnalazioneRepository {

    private final Map<Long, Segnalazione> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Segnalazione add(Segnalazione segnalazione) {
        if (segnalazione.getId() == null) {
            segnalazione.setId(idGenerator.getAndIncrement());
        }
        storage.put(segnalazione.getId(), segnalazione);
        return segnalazione;
    }

    public Optional<Segnalazione> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void modifyRecord(Segnalazione segnalazione) {
        storage.put(segnalazione.getId(), segnalazione);
    }

    public List<Segnalazione> getAllSegnalazioniByHackathon(Long hackathonId) {
        List<Segnalazione> result = new ArrayList<>();
        for (Segnalazione s : storage.values()) {
            if (s.getHackathonId().equals(hackathonId)) {
                result.add(s);
            }
        }
        return result;
    }

    public void deleteAll() {
        storage.clear();
    }
}
