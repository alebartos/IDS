package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.service.ValutazioneService;

/**
 * Handler per la gestione delle valutazioni delle sottomissioni.
 */
public class ValutazioneHandler {

    private final ValutazioneService valutazioneService;

    public ValutazioneHandler(ValutazioneService valutazioneService) {
        this.valutazioneService = valutazioneService;
    }

    /**
     * Conferma e registra la valutazione di una sottomissione.
     * @param giudiceId l'ID del giudice
     * @param sottomissioneId l'ID della sottomissione
     * @param punteggio il punteggio assegnato
     * @param giudizio il giudizio testuale
     * @return Result con la sottomissione aggiornata
     */
    public Result<Sottomissione> confermaSottomissione(Long giudiceId, Long sottomissioneId, int punteggio, String giudizio) {
        try {
            DatiValutazione datiValutazione = valutazioneService.creaDTO(punteggio, giudizio);
            Sottomissione sottomissione = valutazioneService.valutaSottomissione(giudiceId, sottomissioneId, datiValutazione);
            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
