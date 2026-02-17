package it.unicam.ids.repository;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InvitoRepository {

    private final Map<Long, Invito> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Invito add(Invito invito) {
        if (invito.getId() == null) {
            invito.setId(idGenerator.getAndIncrement());
        }
        storage.put(invito.getId(), invito);
        return invito;
    }

    public void modifyRecord(Invito invito) {
        storage.put(invito.getId(), invito);
    }

    public Optional<Invito> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Invito> findAll() {
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

    public Optional<Invito> findByTeamAndDestinatario(Long teamId, Long destinatarioId) {
        return storage.values().stream()
                .filter(invito -> invito.getTeamId() != null &&
                        invito.getTeamId().equals(teamId) &&
                        invito.getDestinatario() != null &&
                        invito.getDestinatario().equals(destinatarioId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .findFirst();
    }

    public List<Invito> findByDestinatarioId(Long destinatarioId) {
        return storage.values().stream()
                .filter(invito -> invito.getDestinatario() != null &&
                        invito.getDestinatario().equals(destinatarioId))
                .collect(Collectors.toList());
    }

    public List<Invito> findByTeamId(Long teamId) {
        return storage.values().stream()
                .filter(invito -> invito.getTeamId() != null && invito.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    public List<Invito> findPendingByDestinatarioId(Long destinatarioId) {
        return storage.values().stream()
                .filter(invito -> invito.getDestinatario() != null &&
                        invito.getDestinatario().equals(destinatarioId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .collect(Collectors.toList());
    }

    public void rifiutaAltriInviti(Long destinatarioId, Long invitoAccettatoId) {
        storage.values().stream()
                .filter(invito -> invito.getDestinatario() != null &&
                        invito.getDestinatario().equals(destinatarioId) &&
                        !invito.getId().equals(invitoAccettatoId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .forEach(invito -> invito.setStato(StatoInvito.RIFIUTATO));
    }
}
