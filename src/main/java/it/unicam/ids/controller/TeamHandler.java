package it.unicam.ids.controller;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
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
    public Result<List<Utente>> getMembriTeam(Long id) {
        try {
            List<Utente> membri = teamService.getMembriTeam(id);
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

    /**
     * Aggiunge un membro al team. Solo il leader può aggiungere membri.
     * Endpoint futuro: POST /api/team/{id}/membri
     * @param teamId ID del team
     * @param membroId ID del membro da aggiungere
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader)
     */
    public Result<Team> aggiungiMembro(Long teamId, Long membroId, Long richiedenteId) {
        try {
            Team team = teamService.aggiungiMembro(teamId, membroId, richiedenteId);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Rimuove un membro dal team. Solo il leader può rimuovere membri.
     * Endpoint futuro: DELETE /api/team/{id}/membri/{membroId}
     * @param teamId ID del team
     * @param membroId ID del membro da rimuovere
     * @param richiedenteId ID dell'utente che effettua l'operazione (deve essere il leader)
     */
    public Result<Team> rimuoviMembro(Long teamId, Long membroId, Long richiedenteId) {
        try {
            Team team = teamService.rimuoviMembro(teamId, membroId, richiedenteId);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
