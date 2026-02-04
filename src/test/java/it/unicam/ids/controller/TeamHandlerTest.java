package it.unicam.ids.controller;


import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.LeaderRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamHandlerTest {

    private TeamHandler teamHandler;
    private TeamService teamService;
    private TeamRepository teamRepository;
    private LeaderRepository leaderRepository;
    private Leader leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        leaderRepository = new LeaderRepository();
        teamService = new TeamService(teamRepository, leaderRepository);
        teamHandler = new TeamHandler(teamService);

        leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = leaderRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        TeamRequest request = new TeamRequest("Team Alpha", "Test team", leader.getId());

        Result<Team> response = teamHandler.creaTeam(request);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("Team Alpha", response.getData().getNome());
    }

    @Test
    void testGetDettagliTeam() {
        TeamRequest request = new TeamRequest("Team Beta", "Details test", leader.getId());
        Team team = teamService.creaTeam(request);

        Result<Team> response = teamHandler.getDettagliTeam(team.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertEquals("Team Beta", response.getData().getNome());
    }

    @Test
    void testGetDettagliTeamNotFound() {
        Result<Team> response = teamHandler.getDettagliTeam(99999L);

        assertEquals(404, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testEsisteTeam() {
        TeamRequest request = new TeamRequest("Team Gamma", "Exists test", leader.getId());
        teamService.creaTeam(request);

        Result<Boolean> existsResponse = teamHandler.esisteTeam("Team Gamma");
        Result<Boolean> notExistsResponse = teamHandler.esisteTeam("NonEsistente");

        assertTrue(existsResponse.getData());
        assertFalse(notExistsResponse.getData());
    }
}
