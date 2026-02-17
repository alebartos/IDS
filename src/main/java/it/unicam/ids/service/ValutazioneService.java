package it.unicam.ids.service;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.SottomissioneRepository;

import java.time.LocalDate;

public class ValutazioneService {

    private final SottomissioneRepository sottomissioneRepo;

    public ValutazioneService(SottomissioneRepository sottomissioneRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
    }

    public DatiValutazione creaDTO(int punteggio, String giudizio) {
        return new DatiValutazione(punteggio, giudizio, LocalDate.now());
    }

    public void checkValidità(DatiValutazione datiValutazione) {
        if (datiValutazione == null) {
            throw new IllegalArgumentException("I parametri non sono validi");
        }
        if (datiValutazione.getGiudizio() == null || datiValutazione.getGiudizio().isEmpty()) {
            throw new IllegalArgumentException("I parametri non sono validi");
        }
    }

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
        sottomissioneRepo.modifyRecord(sottomissione);

        return sottomissione;
    }
}
