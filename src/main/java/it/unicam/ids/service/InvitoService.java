package it.unicam.ids.service;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;

import java.util.List;

/**
 * Service per la gestione degli Inviti.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class InvitoService {

    private final InvitoRepository invitoRepository;
    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public InvitoService(InvitoRepository invitoRepository, TeamRepository teamRepository, UtenteRepository utenteRepository) {
        this.invitoRepository = invitoRepository;
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Invita un membro al team. Solo il leader del team può invitare.
     * @param teamId ID del team
     * @param destinatarioId ID dell'utente da invitare
     * @param richiedenteId ID dell'utente che sta effettuando l'invito (deve essere il leader)
     * @return l'invito creato
     */
    public Invito invitaMembro(Long teamId, Long destinatarioId, Long richiedenteId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        // Verifica che il richiedente sia il leader del team
        if (team.getLeaderId() == null || !team.getLeaderId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può invitare nuovi membri");
        }

        Utente destinatario = utenteRepository.findById(destinatarioId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (invitoRepository.findByTeamAndDestinatario(teamId, destinatarioId).isPresent()) {
            throw new IllegalArgumentException("Esiste già un invito pendente per questo utente in questo team");
        }

        Invito invito = new Invito(team, destinatario);
        return invitoRepository.save(invito);
    }

    public Invito getInvito(Long invitoId) {
        return invitoRepository.findById(invitoId)
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato"));
    }

    /**
     * Accetta un invito. Solo il destinatario dell'invito può accettarlo.
     * @param invitoId ID dell'invito
     * @param richiedenteId ID dell'utente che sta accettando (deve essere il destinatario)
     */
    public void accettaInvito(Long invitoId, Long richiedenteId) {
        Invito invito = getInvito(invitoId);

        // Verifica che il richiedente sia il destinatario dell'invito
        if (!invito.getDestinatarioId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il destinatario può accettare l'invito");
        }

        if (invito.getStato() != StatoInvito.IN_ATTESA) {
            throw new IllegalArgumentException("L'invito non è più in attesa");
        }

        invito.accetta();
        invitoRepository.save(invito);

        // Aggiungi il membro al team
        Team team = invito.getTeam();
        if (team != null) {
            team.addMembro(invito.getDestinatarioId());
            teamRepository.save(team);
        }

        // Aggiungi il ruolo MEMBRO_TEAM all'utente
        Utente destinatario = invito.getDestinatario();
        if (destinatario != null) {
            destinatario.addRuolo(Ruolo.MEMBRO_TEAM);
            utenteRepository.save(destinatario);
        }

        invitoRepository.chiudiAltriInviti(invito.getDestinatarioId(), invitoId);
    }

    /**
     * Rifiuta un invito. Solo il destinatario dell'invito può rifiutarlo.
     * @param invitoId ID dell'invito
     * @param richiedenteId ID dell'utente che sta rifiutando (deve essere il destinatario)
     */
    public void rifiutaInvito(Long invitoId, Long richiedenteId) {
        Invito invito = getInvito(invitoId);

        // Verifica che il richiedente sia il destinatario dell'invito
        if (!invito.getDestinatarioId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il destinatario può rifiutare l'invito");
        }

        if (invito.getStato() != StatoInvito.IN_ATTESA) {
            throw new IllegalArgumentException("L'invito non è più in attesa");
        }

        invito.rifiuta();
        invitoRepository.save(invito);
    }

    /**
     * Gestisce un invito (accetta o rifiuta). Solo il destinatario può gestirlo.
     * @param invitoId ID dell'invito
     * @param risposta "ACCETTATO" o "RIFIUTATO"
     * @param richiedenteId ID dell'utente che sta gestendo l'invito (deve essere il destinatario)
     */
    public void gestisciInvito(Long invitoId, String risposta, Long richiedenteId) {
        if ("ACCETTATO".equalsIgnoreCase(risposta)) {
            accettaInvito(invitoId, richiedenteId);
        } else if ("RIFIUTATO".equalsIgnoreCase(risposta)) {
            rifiutaInvito(invitoId, richiedenteId);
        } else {
            throw new IllegalArgumentException("Risposta non valida. Usare ACCETTATO o RIFIUTATO");
        }
    }

    public List<Invito> getInvitiPendentiPerUtente(Long utenteId) {
        return invitoRepository.findPendingByDestinatarioId(utenteId);
    }

    public List<Invito> getInvitiPerTeam(Long teamId) {
        return invitoRepository.findByTeamId(teamId);
    }
}
