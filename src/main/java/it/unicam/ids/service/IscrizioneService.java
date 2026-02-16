package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.TeamRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service per la gestione delle iscrizioni ai hackathon.
 */
public class IscrizioneService {

    private final TeamRepository teamRepo;
    private final HackathonRepository hackathonRepo;

    public IscrizioneService(TeamRepository teamRepo, HackathonRepository hackathonRepo) {
        this.teamRepo = teamRepo;
        this.hackathonRepo = hackathonRepo;
    }

    /**
     * Recupera i dettagli di un hackathon.
     * @param hackathonId ID dell'hackathon
     * @return l'hackathon trovato
     */
    public Hackathon getDettagliHackathon(Long hackathonId) {
        return hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }

    /**
     * Verifica che il team non sia già iscritto all'hackathon.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     */
    public void checkMaxTeam(Long teamId, Long hackathonId) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        if (hackathon.getTeamIds().contains(teamId)) {
            throw new IllegalArgumentException("Il team è già iscritto a questo hackathon");
        }
    }

    /**
     * Verifica che il numero di partecipanti selezionati non superi il massimo consentito.
     * @param maxMembriTeam il numero massimo di membri
     * @param size il numero di partecipanti selezionati
     * @return true se il numero è valido, false altrimenti
     */
    public boolean verificaMaxMembri(int maxMembriTeam, int size) {
        return size <= maxMembriTeam;
    }

    /**
     * Recupera la lista dei membri del team e il numero massimo di partecipanti consentiti.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @return una mappa con "membri" (List di Long) e "maxMembriTeam" (int)
     */
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

    /**
     * Iscrive un team a un hackathon con i partecipanti selezionati.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param partecipanti lista degli ID degli utenti selezionati come partecipanti
     */
    public void iscriviTeam(Long teamId, Long hackathonId, List<Long> partecipanti) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);

        checkValidità(hackathon);
        checkMaxTeam(teamId, hackathonId);

        teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        if (!verificaMaxMembri(hackathon.getMaxMembriTeam(), partecipanti.size())) {
            throw new IllegalArgumentException("Numero di partecipanti superiore al massimo consentito");
        }

        hackathon.getTeamIds().add(teamId);
        hackathonRepo.save(hackathon);
    }

    /**
     * Verifica la validità dell'hackathon per le iscrizioni.
     * @param hackathon l'hackathon da verificare
     */
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
