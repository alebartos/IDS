package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SegnalazioneService {

    private final SegnalazioneRepository segnalazioneRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;

    public SegnalazioneService(SegnalazioneRepository segnalazioneRepo, HackathonRepository hackathonRepo,
                               UtenteRepository utenteRepo, TeamRepository teamRepo) {
        this.segnalazioneRepo = segnalazioneRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
    }

    public void segnala(Long teamId, String descrizione, Long hackathonId) {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setDescrizione(descrizione);
        segnalazione.setTeamId(teamId);
        segnalazione.setHackathonId(hackathonId);
        segnalazioneRepo.save(segnalazione);

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        Utente organizzatore = utenteRepo.findById(hackathon.getOrganizzatoreId())
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));

        inviaMail(organizzatore.getEmail(), "Nuova segnalazione");
    }

    public List<Segnalazione> getSegnalazioni(Long hackathonId) {
        return segnalazioneRepo.findByHackathonId(hackathonId);
    }

    public void archiviaSegnalazione(Long segnalazioneId) {
        Segnalazione segnalazione = segnalazioneRepo.findById(segnalazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Segnalazione non trovata"));
        segnalazione.setGestita(true);
        segnalazioneRepo.save(segnalazione);
    }

    public void squalificaTeam(Long segnalazioneId, Long teamId, Long hackathonId) {
        Segnalazione segnalazione = segnalazioneRepo.findById(segnalazioneId)
                .orElseThrow(() -> new IllegalArgumentException("Segnalazione non trovata"));

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        if (!hackathon.getTeamIds().contains(teamId)) {
            throw new IllegalArgumentException("Il team non partecipa a questo hackathon");
        }

        // Rimuovi il team dall'hackathon
        hackathon.getTeamIds().remove(teamId);
        hackathonRepo.save(hackathon);

        // Rimuovi ruolo PARTECIPANTE da tutti i membri del team
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        for (Long membroId : team.getMembri()) {
            Utente utente = utenteRepo.findById(membroId).orElse(null);
            if (utente != null) {
                utente.getRuoli().remove(Ruolo.PARTECIPANTE);
                utenteRepo.save(utente);
            }
        }
        // Rimuovi PARTECIPANTE anche dal leader
        Utente leader = utenteRepo.findById(team.getLeaderId()).orElse(null);
        if (leader != null) {
            leader.getRuoli().remove(Ruolo.PARTECIPANTE);
            utenteRepo.save(leader);
        }

        // Segna la segnalazione come gestita
        segnalazione.setGestita(true);
        segnalazioneRepo.save(segnalazione);
    }

    public void inviaMail(String email, String oggetto) {
        System.out.println("[EMAIL] Invio mail a: " + email + " - Oggetto: " + oggetto);
    }

    public void removeTeam(Long teamId) {
        for (Hackathon h : hackathonRepo.findAll()) {
            if (h.getTeamIds().contains(teamId)) {
                h.getTeamIds().remove(teamId);
                hackathonRepo.save(h);
            }
        }
    }

    public void removeRuolo(Ruolo ruolo) {
        List<Utente> utenti = utenteRepo.findByRuoliContaining(ruolo);
        for (Utente u : utenti) {
            u.getRuoli().remove(ruolo);
            utenteRepo.save(u);
        }
    }
}
