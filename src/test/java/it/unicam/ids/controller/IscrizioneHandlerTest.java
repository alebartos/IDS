package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.LeaderRepository;
import it.unicam.ids.repository.OrganizzatoreRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IscrizioneHandlerTest {

    private IscrizioneHandler iscrizioneHandler;
    private HackathonService hackathonService;
    private TeamService teamService;
    private HackathonRepository hackathonRepository;
    private TeamRepository teamRepository;
    private LeaderRepository leaderRepository;
    private OrganizzatoreRepository organizzatoreRepository;

    private Hackathon hackathon;
    private Team team;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        teamRepository = new TeamRepository();
        leaderRepository = new LeaderRepository();
        organizzatoreRepository = new OrganizzatoreRepository();

        hackathonService = new HackathonService(hackathonRepository);
        teamService = new TeamService(teamRepository, leaderRepository);
        iscrizioneHandler = new IscrizioneHandler(hackathonService, teamService);

        Organizzatore organizzatore = new Organizzatore("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore = organizzatoreRepository.save(organizzatore);

        HackathonRequest hackathonRequest = new HackathonRequest(
                "Test Hackathon",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                "Prize",
                5
        );
        hackathon = hackathonService.creaHackathon(organizzatore, hackathonRequest);

        Leader leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = leaderRepository.save(leader);

        TeamRequest teamRequest = new TeamRequest("Team Alpha", "Test team", leader.getId());
        team = teamService.creaTeam(teamRequest);
    }

    @Test
    void testIscriviTeamSuccess() {
        Result<String> response = iscrizioneHandler.iscriviTeam(hackathon.getId(), team.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
    }

    @Test
    void testIscriviTeamHackathonNonValido() {
        Organizzatore org = new Organizzatore("Paolo", "Bianchi", "paolo@example.com", "pass");
        org = organizzatoreRepository.save(org);

        HackathonRequest hackathonRequestPassato = new HackathonRequest(
                "Passato Hackathon",
                "Description",
                LocalDate.now().minusMonths(1),
                LocalDate.now().minusMonths(1).plusDays(3),
                LocalDate.now().minusMonths(2),
                "Roma",
                "Rules",
                "Prize",
                5
        );

        Hackathon hackathonPassato = hackathonService.creaHackathon(org, hackathonRequestPassato);

        Result<String> response = iscrizioneHandler.iscriviTeam(hackathonPassato.getId(), team.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testIscriviTeamNotFound() {
        Result<String> response = iscrizioneHandler.iscriviTeam(99999L, team.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }
}
