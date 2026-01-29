package it.unicam.ids.controller;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Team;
import it.unicam.ids.service.TeamService;

import java.util.List;

/**
 * Handler per le operazioni sui Team.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class TeamHandler {

    private final TeamService teamService;

    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Crea un nuovo team.
     * Endpoint futuro: POST /api/team/crea
     */
    public Result<Team> creaTeam(TeamRequest request) {
        try {
            Team team = teamService.creaTeam(request);
            return Result.created(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest("Dati non validi per la creazione del team");
        }
    }

    /**
     * Ottiene i dettagli di un team.
     * Endpoint futuro: GET /api/team/{id}
     */
    public Result<Team> getDettagliTeam(Long id) {
        try {
            Team team = teamService.getDettagliTeam(id);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.notFound("Team non trovato");
        }
    }

    /**
     * Ottiene i membri di un team.
     * Endpoint futuro: GET /api/team/{id}/membri
     */
    public Result<List<Object>> getMembriTeam(Long id) {
        try {
            List<Object> membri = teamService.getMembriTeam(id);
            return Result.success(membri);
        } catch (IllegalArgumentException e) {
            return Result.notFound("Team non trovato");
        }
    }

    /**
     * Verifica se esiste un team con il nome specificato.
     * Endpoint futuro: GET /api/team/esiste/{nome}
     */
    public Result<Boolean> esisteTeam(String nome) {
        boolean existe = teamService.esisteTeamConNome(nome);
        return Result.success(existe);
    }
}
