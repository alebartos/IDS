package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Organizzatore;
import it.unicam.ids.repository.OrganizzatoreRepository;
import it.unicam.ids.service.HackathonService;

/**
 * Handler per le operazioni sugli Hackathon.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class HackathonHandler {

    private final HackathonService hackathonService;
    private final OrganizzatoreRepository organizzatoreRepository;

    public HackathonHandler(HackathonService hackathonService, OrganizzatoreRepository organizzatoreRepository) {
        this.hackathonService = hackathonService;
        this.organizzatoreRepository = organizzatoreRepository;
    }

    /**
     * Crea un nuovo hackathon.
     * Endpoint futuro: POST /api/hackathon/crea
     */
    public Result<Hackathon> creaHackathon(HackathonRequest request, Long organizzatoreId) {
        try {
            Organizzatore organizzatore = organizzatoreRepository.findById(organizzatoreId)
                    .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));

            Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);
            return Result.created(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest("Dati non validi per la creazione dell'hackathon");
        }
    }

    /**
     * Ottiene i dettagli di un hackathon.
     * Endpoint futuro: GET /api/hackathon/{id}
     */
    public Result<Hackathon> getDettagliHackathon(Long id) {
        try {
            Hackathon hackathon = hackathonService.getDettagliHackathon(id);
            return Result.success(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.notFound("Hackathon non trovato");
        }
    }

    /**
     * Ottiene il numero massimo di membri per team.
     * Endpoint futuro: GET /api/hackathon/{id}/max-membri
     */
    public Result<Integer> getMaxMembriTeam(Long id) {
        try {
            int maxMembri = hackathonService.getMaxMembriTeam(id);
            return Result.success(maxMembri);
        } catch (IllegalArgumentException e) {
            return Result.notFound("Hackathon non trovato");
        }
    }

    /**
     * Verifica la validità di un hackathon.
     * Endpoint futuro: GET /api/hackathon/{id}/validita
     */
    public Result<Boolean> checkValidita(Long id) {
        boolean valido = hackathonService.checkValiditaHackathon(id);
        return Result.success(valido);
    }

    /**
     * Verifica se esiste un hackathon con il nome specificato.
     * Endpoint futuro: GET /api/hackathon/esiste/{nome}
     */
    public Result<Boolean> esisteHackathon(String nome) {
        boolean existe = hackathonService.esisteHackathonConNome(nome);
        return Result.success(existe);
    }
}
