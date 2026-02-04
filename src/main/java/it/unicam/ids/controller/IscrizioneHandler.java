package it.unicam.ids.controller;

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
     * Iscrive un team a un hackathon. Solo il leader del team può iscriverlo.
     * Endpoint futuro: POST /api/iscrizioni/iscriviTeam
     * @param hackathonId ID dell'hackathon
     * @param teamId ID del team
     * @param richiedenteId ID dell'utente che effettua l'iscrizione (deve essere il leader del team)
     */
    public Result<String> iscriviTeam(Long hackathonId, Long teamId, Long richiedenteId) {
        try {
            Hackathon hackathon = hackathonService.getDettagliHackathon(hackathonId);
            Team team = teamService.getDettagliTeam(teamId);

            hackathonService.iscriviTeam(hackathon, team, richiedenteId);

            return Result.success("Team iscritto con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
