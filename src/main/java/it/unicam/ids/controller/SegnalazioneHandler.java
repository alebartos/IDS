package it.unicam.ids.controller;

import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.service.SegnalazioneService;

import java.util.List;

public class SegnalazioneHandler {

    private final SegnalazioneService segnalazioneService;

    public SegnalazioneHandler(SegnalazioneService segnalazioneService) {
        this.segnalazioneService = segnalazioneService;
    }

    public Result<String> segnala(Long teamId, String descrizione, Long hackathonId) {
        try {
            segnalazioneService.segnala(teamId, descrizione, hackathonId);
            return Result.success("Segnalazione inviata con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<List<Segnalazione>> getSegnalazioni(Long hackathonId) {
        try {
            List<Segnalazione> segnalazioni = segnalazioneService.getSegnalazioni(hackathonId);
            return Result.success(segnalazioni);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> archiviaSegnalazione(Long segnalazioneId) {
        try {
            segnalazioneService.archiviaSegnalazione(segnalazioneId);
            return Result.success("Segnalazione archiviata con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> squalificaTeam(Long segnalazioneId, Long teamId, Long hackathonId) {
        try {
            segnalazioneService.squalificaTeam(segnalazioneId, teamId, hackathonId);
            return Result.success("Team squalificato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
