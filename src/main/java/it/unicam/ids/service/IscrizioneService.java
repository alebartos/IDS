package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IscrizioneService {

    private final TeamRepository teamRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;

    public IscrizioneService(TeamRepository teamRepo, HackathonRepository hackathonRepo, UtenteRepository utenteRepo) {
        this.teamRepo = teamRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
    }

    public Hackathon getDettagliHackathon(Long hackathonId) {
        return hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }

    public void checkMaxTeam(Long teamId, Long hackathonId) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        if (hackathon.getTeamIds().contains(teamId)) {
            throw new IllegalArgumentException("Il team è già iscritto a questo hackathon");
        }
    }

    public boolean verificaMaxMembri(int maxMembriTeam, int size) { return size <= maxMembriTeam; }

    public Map<String, Object> selezionaPartecipanti(Long teamId, Long hackathonId) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        int maxMembriTeam = hackathon.getMaxMembriTeam();
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        List<Long> membri = team.getMembri();
        Map<String, Object> result = new HashMap<>();
        result.put("membri", membri);
        result.put("maxMembriTeam", maxMembriTeam);
        return result;
    }

    public void iscriviTeam(Long teamId, Long hackathonId, List<Long> partecipanti) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        checkValidità(hackathon);
        checkMaxTeam(teamId, hackathonId);
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        if (!verificaMaxMembri(hackathon.getMaxMembriTeam(), partecipanti.size())) {
            throw new IllegalArgumentException("Numero di partecipanti superiore al massimo consentito");
        }
        hackathon.getTeamIds().add(teamId);
        hackathonRepo.save(hackathon);
        for (Long utenteId : partecipanti) {
            Utente utente = utenteRepo.findById(utenteId).orElse(null);
            if (utente != null && !utente.getRuoli().contains(Ruolo.PARTECIPANTE)) {
                utente.getRuoli().add(Ruolo.PARTECIPANTE);
                utenteRepo.save(utente);
            }
        }
        Utente leader = utenteRepo.findById(team.getLeaderId()).orElse(null);
        if (leader != null && !leader.getRuoli().contains(Ruolo.PARTECIPANTE)) {
            leader.getRuoli().add(Ruolo.PARTECIPANTE);
            utenteRepo.save(leader);
        }
    }

    public void checkValidità(Hackathon hackathon) {
        if (hackathon.getStato() != StatoHackathon.IN_ISCRIZIONE) {
            throw new IllegalArgumentException("L'hackathon non è in fase di iscrizione");
        }
        LocalDate oggi = LocalDate.now();
        if (oggi.isAfter(hackathon.getScadenzaIscrizioni())) {
            throw new IllegalArgumentException("Le iscrizioni per questo hackathon sono chiuse");
        }
    }
}
