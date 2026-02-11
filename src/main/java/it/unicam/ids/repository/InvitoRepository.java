package it.unicam.ids.repository;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repository per la gestione degli Inviti.
 * Utilizza HashMap per lo storage in-memory.
 * Pronto per futura integrazione con Spring Data JPA.
 */
public class InvitoRepository {

    private final Map<Long, Invito> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Invito save(Invito invito) {
        if (invito.getId() == null) {
            invito.setId(idGenerator.getAndIncrement());
        }
        storage.put(invito.getId(), invito);
        return invito;
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
                .filter(invito -> invito.getTeam() != null &&
                        invito.getTeam().getId().equals(teamId) &&
                        invito.getDestinatarioId() != null &&
                        invito.getDestinatarioId().equals(destinatarioId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .findFirst();
    }

    public Optional<Invito> findByTeamAndDestinatario(Long teamId, Utente destinatario) {
        return findByTeamAndDestinatario(teamId, destinatario.getId());
    }

    public List<Invito> findByDestinatarioId(Long destinatarioId) {
        return storage.values().stream()
                .filter(invito -> invito.getDestinatarioId() != null &&
                        invito.getDestinatarioId().equals(destinatarioId))
                .collect(Collectors.toList());
    }

    public List<Invito> findByDestinatario(Utente destinatario) {
        return findByDestinatarioId(destinatario.getId());
    }

    public List<Invito> findByTeamId(Long teamId) {
        return storage.values().stream()
                .filter(invito -> invito.getTeam() != null && invito.getTeam().getId().equals(teamId))
                .collect(Collectors.toList());
    }

    public List<Invito> findPendingByDestinatarioId(Long destinatarioId) {
        return storage.values().stream()
                .filter(invito -> invito.getDestinatarioId() != null &&
                        invito.getDestinatarioId().equals(destinatarioId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .collect(Collectors.toList());
    }

    public List<Invito> findPendingByDestinatario(Utente destinatario) {
        return findPendingByDestinatarioId(destinatario.getId());
    }

    public void chiudiAltriInviti(Long destinatarioId, Long invitoAccettatoId) {
        storage.values().stream()
                .filter(invito -> invito.getDestinatarioId() != null &&
                        invito.getDestinatarioId().equals(destinatarioId) &&
                        !invito.getId().equals(invitoAccettatoId) &&
                        invito.getStato() == StatoInvito.IN_ATTESA)
                .forEach(invito -> invito.setStato(StatoInvito.RIFIUTATO));
    }
}
