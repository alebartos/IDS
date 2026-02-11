package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.service.SottomissioneService;

import java.util.List;

/**
 * Handler per le operazioni sulle Sottomissioni.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class SottomissioneHandler {

    private final SottomissioneService sottomissioneService;

    public SottomissioneHandler(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    /**
     * Gestisce una bozza di sottomissione. Solo il leader del team può gestire le sottomissioni.
     * Endpoint futuro: POST /api/sottomissioni/bozza
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param dati dati del progetto
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader)
     */
    public Result<Sottomissione> gestisciBozza(Long teamId, Long hackathonId, DatiProgetto dati, Long richiedenteId) {
        try {
            Sottomissione sottomissione = sottomissioneService.gestisciBozza(teamId, hackathonId, dati, richiedenteId);
            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Elabora e finalizza una sottomissione. Solo il leader del team può elaborare le sottomissioni.
     * Endpoint futuro: POST /api/sottomissioni/consegna
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @param dati dati del progetto
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader)
     */
    public Result<Sottomissione> elaboraSottomissione(Long teamId, Long hackathonId, DatiProgetto dati, Long richiedenteId) {
        try {
            Sottomissione sottomissione = sottomissioneService.elaboraSottomissione(teamId, hackathonId, dati, richiedenteId);
            return Result.created(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Ottiene una sottomissione per ID.
     * Endpoint futuro: GET /api/sottomissioni/{id}
     */
    public Result<Sottomissione> getSottomissione(Long id) {
        try {
            Sottomissione sottomissione = sottomissioneService.getSottomissione(id);
            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.notFound(e.getMessage());
        }
    }

    /**
     * Ottiene la sottomissione di un team per un hackathon.
     * Endpoint futuro: GET /api/sottomissioni/team/{teamId}/hackathon/{hackathonId}
     */
    public Result<Sottomissione> getSottomissioneByTeamAndHackathon(Long teamId, Long hackathonId) {
        try {
            Sottomissione sottomissione = sottomissioneService.getSottomissioneByTeamAndHackathon(teamId, hackathonId);
            return Result.success(sottomissione);
        } catch (IllegalArgumentException e) {
            return Result.notFound(e.getMessage());
        }
    }

    /**
     * Ottiene tutte le sottomissioni di un hackathon.
     * Endpoint futuro: GET /api/sottomissioni/hackathon/{hackathonId}
     */
    public Result<List<Sottomissione>> getSottomissioniByHackathon(Long hackathonId) {
        try {
            List<Sottomissione> sottomissioni = sottomissioneService.getSottomissioniByHackathon(hackathonId);
            return Result.success(sottomissioni);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
