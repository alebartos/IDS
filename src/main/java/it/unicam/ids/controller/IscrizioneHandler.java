package it.unicam.ids.controller;


import it.unicam.ids.controller.Result;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.TeamService;

/**
 * Handler per le operazioni di iscrizione.
 * Placeholder per futura integrazione con Spring Boot REST Controller.
 */
public class IscrizioneHandler {

    private final HackathonService hackathonService;
    private final TeamService teamService;

    public IscrizioneHandler(HackathonService hackathonService, TeamService teamService) {
        this.hackathonService = hackathonService;
        this.teamService = teamService;
    }

    /**
     * Iscrive un team a un hackathon.
     * Endpoint futuro: POST /api/iscrizioni/iscriviTeam
     */
    public Result<String> iscriviTeam(Long hackathonId, Long teamId) {
        try {
            Hackathon hackathon = hackathonService.getDettagliHackathon(hackathonId);
            Team team = teamService.getDettagliTeam(teamId);

            hackathonService.iscriviTeam(hackathon, team);

            return Result.success("Team iscritto con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
