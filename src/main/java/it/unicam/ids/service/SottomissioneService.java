package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.builder.SottomissioneBuilder;
import it.unicam.ids.repository.SottomissioneRepository;

/**
 * Service per la gestione delle Sottomissioni.
 */
public class SottomissioneService {

    private final SottomissioneRepository sottomissioneRepo;

    public SottomissioneService(SottomissioneRepository sottomissioneRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
    }

    /**
     * Gestisce le bozze di sottomissione. Cerca una bozza esistente o ne crea una nuova.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @return la sottomissione (bozza esistente o nuova)
     */
    public Sottomissione gestisciBozze(Long teamId, Long hackathonId) {
        Sottomissione sottomissione = sottomissioneRepo
                .findByTeamAndHackathon(teamId, hackathonId)
                .orElse(null);

        if (sottomissione != null) {
            if (sottomissione.getStato() == StatoSottomissione.CONSEGNATA) {
                throw new IllegalArgumentException("Sottomissione definitiva già inviata, non è possibile modificarla");
            }
            return sottomissione;
        }

        sottomissione = SottomissioneBuilder.newBuilder()
                .teamId(teamId)
                .hackathonId(hackathonId)
                .stato(StatoSottomissione.BOZZA)
                .build();

        return sottomissioneRepo.save(sottomissione);
    }

    /**
     * Verifica la validità di un link alla repository.
     * @param linkRepository il link da validare
     */
    public void checkValiditàLink(String linkRepository) {
        if (linkRepository == null || linkRepository.isEmpty()) {
            throw new IllegalArgumentException("Il link alla repository è obbligatorio");
        }
        if (!linkRepository.startsWith("http://") && !linkRepository.startsWith("https://")) {
            throw new IllegalArgumentException("Il link alla repository non è valido");
        }
    }

    /**
     * Crea un DTO per i dati del progetto.
     * @param titolo il titolo del progetto
     * @param descrizione la descrizione del progetto
     * @param linkRepository il link alla repository
     * @return il DTO con i dati del progetto
     */
    public DatiProgetto creaDTO(String titolo, String descrizione, String linkRepository) {
        return new DatiProgetto(titolo, descrizione, linkRepository);
    }
}
