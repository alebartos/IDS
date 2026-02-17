package it.unicam.ids.controller;

import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.service.SupportoService;

import java.time.LocalDate;
import java.util.List;

public class SupportoHandler {

    private final SupportoService supportoService;

    public SupportoHandler(SupportoService supportoService) {
        this.supportoService = supportoService;
    }

    public Result<String> inviaRichiestaSupporto(String descrizione, Long utenteId, Long hackathonId) {
        try {
            supportoService.elaboraRichiestaSupporto(descrizione, utenteId, hackathonId);
            return Result.success("Richiesta di supporto inviata con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<List<RichiestaSupporto>> getRichieste(Long hackathonId) {
        try {
            List<RichiestaSupporto> richieste = supportoService.creaListaRichieste(hackathonId);
            return Result.success(richieste);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> prenotaCall(Long richiestaId, LocalDate dataInizio, LocalDate dataFine) {
        try {
            supportoService.prenotaCall(richiestaId, dataInizio, dataFine);
            return Result.success("Call prenotata con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
