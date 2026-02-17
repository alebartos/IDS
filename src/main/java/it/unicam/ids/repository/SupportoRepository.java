package it.unicam.ids.repository;

import it.unicam.ids.model.RichiestaSupporto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SupportoRepository {

    private final Map<Long, RichiestaSupporto> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public RichiestaSupporto add(RichiestaSupporto richiestaSupporto) {
        if (richiestaSupporto.getId() == null) {
            richiestaSupporto.setId(idGenerator.getAndIncrement());
        }
        storage.put(richiestaSupporto.getId(), richiestaSupporto);
        return richiestaSupporto;
    }

    public List<RichiestaSupporto> getAllRichieste(Long hackathonId) {
        return storage.values().stream()
                .filter(r -> r.getHackathonId() != null && r.getHackathonId().equals(hackathonId))
                .collect(Collectors.toList());
    }

    public Optional<RichiestaSupporto> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void modifyRecord(RichiestaSupporto richiestaSupporto) {
        storage.put(richiestaSupporto.getId(), richiestaSupporto);
    }

    public void deleteAll() {
        storage.clear();
    }
}
