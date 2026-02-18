package it.unicam.ids.service;

import it.unicam.ids.builder.InvitoBuilder;
import it.unicam.ids.model.Invito;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InvitoService {

    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;
    private final InvitoRepository invitoRepo;

    public InvitoService(UtenteRepository utenteRepo, TeamRepository teamRepo, InvitoRepository invitoRepo) {
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
        this.invitoRepo = invitoRepo;
    }

    public Invito invitaMembro(String email) {
        Utente destinatario = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        if (!invitoRepo.findByDestinatarioAndStato(destinatario.getId(), StatoInvito.IN_ATTESA).isEmpty()) {
            throw new IllegalArgumentException("Esiste già un invito pendente per questo utente");
        }
        Invito invito = InvitoBuilder.newBuilder()
                .destinatario(destinatario.getId())
                .build();
        return invitoRepo.save(invito);
    }

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
            Team team = invito.getTeamId() != null ? teamRepo.findById(invito.getTeamId()).orElse(null) : null;
            if (team != null) {
                team.getMembri().add(invito.getDestinatario());
                teamRepo.save(team);
            }
            Utente destinatario = utenteRepo.findById(invito.getDestinatario()).orElse(null);
            if (destinatario != null) {
                destinatario.getRuoli().add(Ruolo.MEMBRO_TEAM);
                utenteRepo.save(destinatario);
            }
            // Rifiuta altri inviti pendenti
            List<Invito> altriInviti = invitoRepo.findByDestinatarioAndStato(invito.getDestinatario(), StatoInvito.IN_ATTESA);
            for (Invito altro : altriInviti) {
                if (!altro.getId().equals(invitoId)) {
                    altro.setStato(StatoInvito.RIFIUTATO);
                    invitoRepo.save(altro);
                }
            }
        } else if ("RIFIUTATO".equalsIgnoreCase(risposta)) {
            invito.setStato(StatoInvito.RIFIUTATO);
            invito.setDataRisposta(LocalDate.now());
            invitoRepo.save(invito);
        } else {
            throw new IllegalArgumentException("Risposta non valida. Usare ACCETTATO o RIFIUTATO");
        }
    }

    public boolean checkRuolo(Ruolo ruolo) {
        return ruolo != null;
    }
}
