package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.builder.SottomissioneBuilder;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.TeamRepository;

import java.util.List;

/**
 * Service per la gestione delle Sottomissioni.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class SottomissioneService {

    private final SottomissioneRepository sottomissioneRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    public SottomissioneService(SottomissioneRepository sottomissioneRepository,
                                TeamRepository teamRepository,
                                HackathonRepository hackathonRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
    }

    /**
     * Gestisce una bozza di sottomissione. Crea una nuova bozza o aggiorna quella esistente.
     * Solo il leader del team può gestire le sottomissioni.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param dati dati del progetto
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader del team)
     * @return la sottomissione creata o aggiornata
     */
    public Sottomissione gestisciBozza(Long teamId, Long hackathonId, DatiProgetto dati, Long richiedenteId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        // Verifica che il richiedente sia il leader del team
        if (team.getLeaderId() == null || !team.getLeaderId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può gestire le sottomissioni");
        }

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        // Verifica che l'hackathon sia in corso
        if (hackathon.getStato() != StatoHackathon.IN_CORSO) {
            throw new IllegalArgumentException("L'hackathon non è in corso, non è possibile sottomettere progetti");
        }

        // Cerca una sottomissione esistente per questo team e hackathon
        Sottomissione sottomissione = sottomissioneRepository
                .findByTeamAndHackathon(teamId, hackathonId)
                .orElse(null);

        if (sottomissione != null) {
            // Verifica che sia ancora una bozza
            if (sottomissione.isConsegnata()) {
                throw new IllegalArgumentException("Sottomissione definitiva già inviata, non è possibile modificarla");
            }
            // Aggiorna la bozza esistente
            sottomissione.setDatiProgetto(dati);
        } else {
            // Crea una nuova bozza usando il Builder
            sottomissione = SottomissioneBuilder.newBuilder()
                    .teamId(teamId)
                    .hackathonId(hackathonId)
                    .datiProgetto(dati)
                    .stato(StatoSottomissione.BOZZA)
                    .build();
        }

        return sottomissioneRepository.save(sottomissione);
    }

    /**
     * Elabora e finalizza una sottomissione, cambiando lo stato da BOZZA a CONSEGNATA.
     * Solo il leader del team può elaborare le sottomissioni.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param dati dati del progetto (per aggiornamento finale)
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader del team)
     * @return la sottomissione finalizzata
     */
    public Sottomissione elaboraSottomissione(Long teamId, Long hackathonId, DatiProgetto dati, Long richiedenteId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        // Verifica che il richiedente sia il leader del team
        if (team.getLeaderId() == null || !team.getLeaderId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può elaborare le sottomissioni");
        }

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        // Verifica che l'hackathon sia in corso
        if (hackathon.getStato() != StatoHackathon.IN_CORSO) {
            throw new IllegalArgumentException("L'hackathon non è in corso, non è possibile sottomettere progetti");
        }

        // Cerca la sottomissione esistente
        Sottomissione sottomissione = sottomissioneRepository
                .findByTeamAndHackathon(teamId, hackathonId)
                .orElse(null);

        if (sottomissione == null) {
            // Crea e consegna direttamente usando il Builder
            sottomissione = SottomissioneBuilder.newBuilder()
                    .teamId(teamId)
                    .hackathonId(hackathonId)
                    .datiProgetto(dati)
                    .stato(StatoSottomissione.CONSEGNATA)
                    .build();
        } else if (sottomissione.isConsegnata()) {
            throw new IllegalArgumentException("Sottomissione definitiva già inviata");
        } else {
            // Aggiorna i dati e imposta come consegnata
            sottomissione.setDatiProgetto(dati);
            sottomissione.setStato(StatoSottomissione.CONSEGNATA);
        }

        return sottomissioneRepository.save(sottomissione);
    }

    /**
     * Ottiene una sottomissione per ID.
     */
    public Sottomissione getSottomissione(Long id) {
        return sottomissioneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));
    }

    /**
     * Ottiene la sottomissione di un team per un hackathon.
     */
    public Sottomissione getSottomissioneByTeamAndHackathon(Long teamId, Long hackathonId) {
        return sottomissioneRepository.findByTeamAndHackathon(teamId, hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));
    }

    /**
     * Ottiene tutte le sottomissioni di un hackathon.
     */
    public List<Sottomissione> getSottomissioniByHackathon(Long hackathonId) {
        return sottomissioneRepository.findByHackathonId(hackathonId);
    }
}
