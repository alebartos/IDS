package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.service.HackathonService;

import java.time.LocalDate;
import java.util.List;

public class HackathonHandler {

    private final HackathonService hackathonService;

    public HackathonHandler(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public Result<Hackathon> creaHackathonRequest(String nome, LocalDate dataInizio, LocalDate dataFine,
                                                  String descrizione, String regolamento,
                                                  LocalDate scadenzaIscrizioni, int maxPartecipanti,
                                                  double premio, Long organizzatoreId) {
        try {
            Hackathon hackathon = hackathonService.createHackathon(nome, descrizione, dataInizio, dataFine, scadenzaIscrizioni, maxPartecipanti, premio, organizzatoreId);
            return Result.created(hackathon);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> assegnaGiudice(Long hackathonId, String email, Long organizzatoreId) {
        try {
            hackathonService.assegnaGiudice(hackathonId, email, organizzatoreId);
            return Result.success("Giudice assegnato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> assegnaMentore(String email, Long organizzatoreId) {
        try {
            hackathonService.assegnaMentore(email, organizzatoreId);
            return Result.success("Mentore assegnato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> refreshDettagli() {
        return Result.success("Dettagli aggiornati");
    }

    public Result<String> cambiaStato(Long hackathonId, StatoHackathon nuovoStato) {
        try {
            hackathonService.modifcaStato(hackathonId, nuovoStato);
            return Result.success("Stato aggiornato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<String> proclamaVincitore(Long hackathonId, Long teamId) {
        try {
            hackathonService.proclamaVincitore(hackathonId, teamId);
            return Result.success("Vincitore proclamato con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<List<Hackathon>> getHackathons() {
        try {
            List<Hackathon> hackathons = hackathonService.getHackathons();
            return Result.success(hackathons);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    public Result<List<Team>> getTeams(Long hackathonId) {
        try {
            List<Team> teams = hackathonService.getTeams(hackathonId);
            return Result.success(teams);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
