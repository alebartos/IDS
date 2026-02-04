package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;

/**
 * Handler per le operazioni sugli Hackathon.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class HackathonHandler {

    private final HackathonService hackathonService;
    private final UtenteRepository utenteRepository;

    public HackathonHandler(HackathonService hackathonService, UtenteRepository utenteRepository) {
        this.hackathonService = hackathonService;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un nuovo hackathon.
     * Endpoint futuro: POST /api/hackathon/crea
     */
    public Result<Hackathon> creaHackathon(HackathonRequest request, Long organizzatoreId) {
        try {
            Utente organizzatore = utenteRepository.findById(organizzatoreId)
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            if (!organizzatore.hasRuolo(Ruolo.ORGANIZZATORE)) {
                return Result.badRequest("L'utente deve avere il ruolo ORGANIZZATORE");
            }

            Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);
            return Result.created(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
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

    /**
     * Assegna un giudice a un hackathon. Solo un organizzatore può assegnare giudici.
     * Endpoint futuro: POST /api/hackathon/{id}/giudice
     * @param hackathonId ID dell'hackathon
     * @param giudiceId ID dell'utente da assegnare come giudice
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere ORGANIZZATORE)
     */
    public Result<Hackathon> assegnaGiudice(Long hackathonId, Long giudiceId, Long richiedenteId) {
        try {
            Hackathon hackathon = hackathonService.assegnaGiudice(hackathonId, giudiceId, richiedenteId);
            return Result.success(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Rimuove il giudice da un hackathon. Solo un organizzatore può rimuovere giudici.
     * Endpoint futuro: DELETE /api/hackathon/{id}/giudice
     * @param hackathonId ID dell'hackathon
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere ORGANIZZATORE)
     */
    public Result<Hackathon> rimuoviGiudice(Long hackathonId, Long richiedenteId) {
        try {
            Hackathon hackathon = hackathonService.rimuoviGiudice(hackathonId, richiedenteId);
            return Result.success(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Aggiunge un membro allo staff. Solo un organizzatore può aggiungere staff.
     * Endpoint futuro: POST /api/hackathon/{id}/staff
     * @param hackathonId ID dell'hackathon
     * @param utenteId ID dell'utente da aggiungere allo staff
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere ORGANIZZATORE)
     */
    public Result<Hackathon> aggiungiMembroStaff(Long hackathonId, Long utenteId, Long richiedenteId) {
        try {
            Hackathon hackathon = hackathonService.aggiungiMembroStaff(hackathonId, utenteId, richiedenteId);
            return Result.success(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Rimuove un membro dallo staff. Solo un organizzatore può rimuovere staff.
     * Endpoint futuro: DELETE /api/hackathon/{id}/staff/{utenteId}
     * @param hackathonId ID dell'hackathon
     * @param utenteId ID dell'utente da rimuovere dallo staff
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere ORGANIZZATORE)
     */
    public Result<Hackathon> rimuoviMembroStaff(Long hackathonId, Long utenteId, Long richiedenteId) {
        try {
            Hackathon hackathon = hackathonService.rimuoviMembroStaff(hackathonId, utenteId, richiedenteId);
            return Result.success(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
