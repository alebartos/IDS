package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.service.HackathonService;

import java.time.LocalDate;

/**
 * Handler per le operazioni sugli Hackathon.
 */
public class HackathonHandler {

    private final HackathonService hackathonService;

    public HackathonHandler(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    /**
     * Crea un nuovo hackathon.
     * @param nome il nome
     * @param dataInizio la data di inizio
     * @param dataFine la data di fine
     * @param descrizione la descrizione
     * @param regolamento il regolamento
     * @param scadenzaIscrizioni la scadenza delle iscrizioni
     * @param maxPartecipanti il numero massimo di partecipanti
     * @param premio il premio
     * @return Result con l'hackathon creato
     */
    public Result<Hackathon> creaHackathonRequest(String nome, LocalDate dataInizio, LocalDate dataFine,
                                                  String descrizione, String regolamento,
                                                  LocalDate scadenzaIscrizioni, int maxPartecipanti,
                                                  double premio, Long organizzatoreId) {
        try {
            Hackathon hackathon = hackathonService.createHackathon(nome, descrizione, dataInizio, dataFine, maxPartecipanti, premio, organizzatoreId);
            return Result.created(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Assegna un giudice a un hackathon tramite email.
     * @param hackathonId ID dell'hackathon
     * @param email email dell'utente da assegnare come giudice
     * @param organizzatoreId ID dell'organizzatore
     */
    public Result<String> assegnaGiudice(Long hackathonId, String email, Long organizzatoreId) {
        try {
            hackathonService.assegnaGiudice(hackathonId, email, organizzatoreId);
            return Result.success("Giudice assegnato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Assegna un mentore tramite email.
     * @param email email dell'utente da assegnare come mentore
     * @param organizzatoreId ID dell'organizzatore
     */
    public Result<String> assegnaMentore(String email, Long organizzatoreId) {
        try {
            hackathonService.assegnaMentore(email, organizzatoreId);
            return Result.success("Mentore assegnato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Aggiorna i dettagli dell'hackathon.
     */
    public Result<String> refreshDettagli() {
        return Result.success("Dettagli aggiornati");
    }

    /**
     * Cambia lo stato di un hackathon.
     * @param hackathonId ID dell'hackathon
     * @param nuovoStato il nuovo stato
     */
    public Result<String> cambiaStato(Long hackathonId, StatoHackathon nuovoStato) {
        try {
            hackathonService.modifcaStato(hackathonId, nuovoStato);
            return Result.success("Stato aggiornato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Proclama il team vincitore di un hackathon.
     * @param hackathonId ID dell'hackathon
     * @param teamId ID del team vincitore
     */
    public Result<String> proclamaVincitore(Long hackathonId, Long teamId) {
        try {
            hackathonService.proclamaVincitore(hackathonId, teamId);
            return Result.success("Vincitore proclamato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
