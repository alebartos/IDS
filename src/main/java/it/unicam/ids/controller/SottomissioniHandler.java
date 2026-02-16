package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.service.SottomissioneService;

/**
 * Handler per le operazioni sulle Sottomissioni.
 */
public class SottomissioniHandler {

    private final SottomissioneService sottomissioneService;

    public SottomissioniHandler(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    /**
     * Carica una bozza di sottomissione.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param datiProgetto i dati del progetto
     * @param isDefinitiva se true, la sottomissione viene consegnata definitivamente
     */
    public Result<Sottomissione> caricaBozza(Long teamId, Long hackathonId, DatiProgetto datiProgetto, boolean isDefinitiva) {
        try {
            Sottomissione sottomissione = sottomissioneService.gestisciBozze(teamId, hackathonId);
            sottomissione.setDatiProgetto(datiProgetto);

            if (isDefinitiva) {
                sottomissione.setStato(StatoSottomissione.CONSEGNATA);
            }

            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Gestisce la sottomissione di un progetto.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param datiProgetto i dati del progetto
     * @param isDefinitiva se true, la sottomissione viene consegnata definitivamente
     */
    public Result<Sottomissione> sottomissioneHandler(Long teamId, Long hackathonId, DatiProgetto datiProgetto, boolean isDefinitiva) {
        try {
            sottomissioneService.checkValiditàLink(datiProgetto.getLinkRepository());

            Sottomissione sottomissione = sottomissioneService.gestisciBozze(teamId, hackathonId);
            sottomissione.setDatiProgetto(datiProgetto);

            if (isDefinitiva) {
                sottomissione.setStato(StatoSottomissione.CONSEGNATA);
            }

            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
