package it.unicam.ids.service;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class ValutazioneService {

    private final SottomissioneRepository sottomissioneRepo;
    private final HackathonRepository hackathonRepo;

    public ValutazioneService(SottomissioneRepository sottomissioneRepo, HackathonRepository hackathonRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
        this.hackathonRepo = hackathonRepo;
    }

    public DatiValutazione creaDTO(int punteggio, String giudizio) {
        return new DatiValutazione(punteggio, giudizio, LocalDate.now());
    }

    public void checkValidità(DatiValutazione datiValutazione) {
        if (datiValutazione == null) throw new IllegalArgumentException("I parametri non sono validi");
        if (datiValutazione.getGiudizio() == null || datiValutazione.getGiudizio().isEmpty())
            throw new IllegalArgumentException("I parametri non sono validi");
    }

    public Sottomissione valutaSottomissione(Long giudiceId, Long sottomissioneId, DatiValutazione datiValutazione) {
        if (giudiceId == null || sottomissioneId == null) throw new IllegalArgumentException("I parametri non sono validi");
        checkValidità(datiValutazione);
        Sottomissione sottomissione = sottomissioneRepo.findById(sottomissioneId)
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));
        Hackathon hackathon = hackathonRepo.findById(sottomissione.getHackathonId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (hackathon.getStato() != StatoHackathon.IN_VALUTAZIONE) {
            throw new IllegalArgumentException("L'hackathon non è in fase di valutazione");
        }
        if (sottomissione.getStato() == StatoSottomissione.VALUTATA)
            throw new IllegalArgumentException("La sottomissione è già stata valutata");
        if (sottomissione.getStato() != StatoSottomissione.CONSEGNATA)
            throw new IllegalArgumentException("La sottomissione non è in stato CONSEGNATA");
        sottomissione.setDatiValutazione(datiValutazione);
        sottomissione.setStato(StatoSottomissione.VALUTATA);
        sottomissioneRepo.save(sottomissione);
        return sottomissione;
    }
}
