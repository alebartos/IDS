package it.unicam.ids.controller;

import it.unicam.ids.model.Invito;
import it.unicam.ids.service.InvitoService;

import java.util.List;

/**
 * Handler per le operazioni sugli inviti.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class InvitoHandler {

    private final InvitoService invitoService;

    public InvitoHandler(InvitoService invitoService) {
        this.invitoService = invitoService;
    }

    /**
     * Invia un invito a un utente per unirsi a un team. Solo il leader può invitare.
     * Endpoint futuro: POST /api/inviti/invita
     * @param teamId ID del team
     * @param destinatarioId ID dell'utente da invitare
     * @param richiedenteId ID dell'utente che effettua l'invito (deve essere il leader)
     */
    public Result<Invito> invitaMembro(Long teamId, Long destinatarioId, Long richiedenteId) {
        try {
            Invito invito = invitoService.invitaMembro(teamId, destinatarioId, richiedenteId);
            return Result.created(invito);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Gestisce la risposta a un invito (accetta o rifiuta). Solo il destinatario può rispondere.
     * Endpoint futuro: POST /api/inviti/gestisci
     * @param invitoId ID dell'invito
     * @param risposta "ACCEPTED" o "REJECTED"
     * @param richiedenteId ID dell'utente che risponde (deve essere il destinatario)
     */
    public Result<String> gestisciInvito(Long invitoId, String risposta, Long richiedenteId) {
        try {
            invitoService.gestisciInvito(invitoId, risposta, richiedenteId);
            return Result.success("Invito gestito con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Ottiene i dettagli di un invito.
     * Endpoint futuro: GET /api/inviti/{id}
     */
    public Result<Invito> getInvito(Long invitoId) {
        try {
            Invito invito = invitoService.getInvito(invitoId);
            return Result.success(invito);
        } catch (IllegalArgumentException e) {
            return Result.notFound(e.getMessage());
        }
    }

    /**
     * Ottiene gli inviti pendenti per un utente.
     * Endpoint futuro: GET /api/inviti/pendenti/{utenteId}
     */
    public Result<List<Invito>> getInvitiPendenti(Long utenteId) {
        try {
            List<Invito> inviti = invitoService.getInvitiPendentiPerUtente(utenteId);
            return Result.success(inviti);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
