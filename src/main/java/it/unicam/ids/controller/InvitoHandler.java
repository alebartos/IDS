package it.unicam.ids.controller;

import it.unicam.ids.service.InvitoService;

/**
 * Handler per le operazioni sugli inviti.
 */
public class InvitoHandler {

    private final InvitoService invitoService;

    public InvitoHandler(InvitoService invitoService) {
        this.invitoService = invitoService;
    }

    /**
     * Invia un invito a un utente per unirsi a un team tramite email.
     * @param email email dell'utente da invitare
     * @param teamId ID del team
     * @param richiedenteId ID dell'utente che effettua l'invito (deve essere il leader)
     */
    public Result<String> invitaMembro(String email, Long teamId, Long richiedenteId) {
        try {
            invitoService.invitaMembro(email, teamId, richiedenteId);
            return Result.success("Invito inviato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Gestisce la risposta a un invito (accetta o rifiuta).
     * @param invitoId ID dell'invito
     * @param risposta "ACCETTATO" o "RIFIUTATO"
     */
    public Result<String> gestisciInvito(Long invitoId, String risposta) {
        try {
            invitoService.gestisciInvito(invitoId, risposta);
            return Result.success("Invito gestito con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
