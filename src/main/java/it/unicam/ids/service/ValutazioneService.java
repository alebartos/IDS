package it.unicam.ids.service;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.SottomissioneRepository;

import java.time.LocalDate;

/**
 * Service per la gestione delle valutazioni delle sottomissioni.
 */
public class ValutazioneService {

    private final SottomissioneRepository sottomissioneRepo;

    public ValutazioneService(SottomissioneRepository sottomissioneRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
    }

    /**
     * Crea un DTO di valutazione a partire da punteggio e giudizio.
     * @param punteggio il punteggio assegnato
     * @param giudizio il giudizio testuale
     * @return il DTO con i dati della valutazione
     */
    public DatiValutazione creaDTO(int punteggio, String giudizio) {
        return new DatiValutazione(punteggio, giudizio, LocalDate.now());
    }

    /**
     * Verifica la validità dei dati di valutazione.
     * @param datiValutazione i dati da validare
     */
    public void checkValidità(DatiValutazione datiValutazione) {
        if (datiValutazione == null) {
            throw new IllegalArgumentException("I parametri non sono validi");
        }
        if (datiValutazione.getGiudizio() == null || datiValutazione.getGiudizio().isEmpty()) {
            throw new IllegalArgumentException("I parametri non sono validi");
        }
    }

    /**
     * Valuta una sottomissione.
     * @param giudiceId l'ID del giudice che valuta
     * @param sottomissioneId l'ID della sottomissione da valutare
     * @param datiValutazione i dati della valutazione
     * @return la sottomissione aggiornata con la valutazione
     */
    public Sottomissione valutaSottomissione(Long giudiceId, Long sottomissioneId, DatiValutazione datiValutazione) {
        if (giudiceId == null || sottomissioneId == null) {
            throw new IllegalArgumentException("I parametri non sono validi");
        }

        checkValidità(datiValutazione);

        Sottomissione sottomissione = sottomissioneRepo.findById(sottomissioneId)
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));

        if (sottomissione.getStato() == StatoSottomissione.VALUTATA) {
            throw new IllegalArgumentException("La sottomissione è già stata valutata");
        }

        if (sottomissione.getStato() != StatoSottomissione.CONSEGNATA) {
            throw new IllegalArgumentException("La sottomissione non è in stato CONSEGNATA");
        }

        sottomissione.setDatiValutazione(datiValutazione);
        sottomissione.setStato(StatoSottomissione.VALUTATA);
        sottomissioneRepo.save(sottomissione);

        return sottomissione;
    }
}
