package it.unicam.ids.controller;

import it.unicam.ids.service.SegnalazioneService;

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
}
