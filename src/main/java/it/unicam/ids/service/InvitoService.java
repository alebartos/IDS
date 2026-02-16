package it.unicam.ids.service;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;

import java.time.LocalDate;

/**
 * Service per la gestione degli Inviti.
 */
public class InvitoService {

    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;
    private final InvitoRepository invitoRepo;

    public InvitoService(UtenteRepository utenteRepo, TeamRepository teamRepo, InvitoRepository invitoRepo) {
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
        this.invitoRepo = invitoRepo;
    }

    /**
     * Invita un membro al team tramite email.
     * @param email email dell'utente da invitare
     * @param teamId ID del team
     * @param richiedenteId ID dell'utente che sta effettuando l'invito (deve essere il leader)
     * @return l'invito creato
     */
    public Invito invitaMembro(String email, Long teamId, Long richiedenteId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        if (team.getLeaderId() == null || !team.getLeaderId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può invitare nuovi membri");
        }

        Utente destinatario = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (invitoRepo.findByTeamAndDestinatario(teamId, destinatario.getId()).isPresent()) {
            throw new IllegalArgumentException("Esiste già un invito pendente per questo utente in questo team");
        }

        Invito invito = new Invito(team, destinatario);
        return invitoRepo.save(invito);
    }

    /**
     * Gestisce un invito (accetta o rifiuta).
     * @param invitoId ID dell'invito
     * @param risposta "ACCETTATO" o "RIFIUTATO"
     */
    public void gestisciInvito(Long invitoId, String risposta) {
        Invito invito = invitoRepo.findById(invitoId)
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato"));

        if (invito.getStato() != StatoInvito.IN_ATTESA) {
            throw new IllegalArgumentException("L'invito non è più in attesa");
        }

        if ("ACCETTATO".equalsIgnoreCase(risposta)) {
            invito.setStato(StatoInvito.ACCETTATO);
            invito.setDataRisposta(LocalDate.now());
            invitoRepo.save(invito);

            Team team = invito.getTeam();
            if (team != null) {
                team.getMembri().add(invito.getDestinatario().getId());
                teamRepo.save(team);
            }

            Utente destinatario = invito.getDestinatario();
            if (destinatario != null) {
                destinatario.getRuoli().add(Ruolo.MEMBRO_TEAM);
                utenteRepo.save(destinatario);
            }

            invitoRepo.chiudiAltriInviti(invito.getDestinatario().getId(), invitoId);
        } else if ("RIFIUTATO".equalsIgnoreCase(risposta)) {
            invito.setStato(StatoInvito.RIFIUTATO);
            invito.setDataRisposta(LocalDate.now());
            invitoRepo.save(invito);
        } else {
            throw new IllegalArgumentException("Risposta non valida. Usare ACCETTATO o RIFIUTATO");
        }
    }
}
