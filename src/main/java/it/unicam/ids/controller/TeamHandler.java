package it.unicam.ids.controller;

import it.unicam.ids.model.Team;
import it.unicam.ids.service.TeamService;

/**
 * Handler per le operazioni sui Team.
 */
public class TeamHandler {

    private final TeamService teamService;

    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Crea un nuovo team.
     * @param nomeTeam il nome del team
     * @param leaderId l'ID del leader
     */
    public Result<Team> creaTeam(String nomeTeam, Long leaderId) {
        try {
            Team team = teamService.createTeam(nomeTeam, leaderId);
            return Result.created(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Rimuove un membro dal team.
     * @param membroId l'ID del membro da rimuovere
     * @param leaderId l'ID del leader che effettua la rimozione
     */
    public Result<Team> rimuoviMembro(Long membroId, Long leaderId) {
        try {
            Team team = teamService.rimuoviMembro(membroId, leaderId);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Nomina un membro del team come viceleader.
     * @param leaderId l'ID del leader che effettua la nomina
     * @param membroId l'ID del membro da nominare viceleader
     */
    public Result<Team> nominaViceleader(Long leaderId, Long membroId) {
        try {
            Team team = teamService.nominaViceleader(leaderId, membroId);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Revoca la nomina di viceleader.
     * @param leaderId l'ID del leader che effettua la revoca
     * @param membroId l'ID del membro a cui revocare il ruolo
     */
    public Result<Team> revocaViceleader(Long leaderId, Long membroId) {
        try {
            Team team = teamService.removeViceleader(leaderId, membroId);
            return Result.success(team);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
