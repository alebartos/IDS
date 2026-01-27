package it.unicam.ids.controller;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Leader;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.LeaderRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamHandlerTest {

    @Autowired
    private TeamHandler teamHandler;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private TeamService teamService;

    private Leader leader;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
        leaderRepository.deleteAll();

        leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = leaderRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        TeamRequest request = new TeamRequest("Team Alpha", "Test team", leader.getId());

        ResponseEntity<?> response = teamHandler.creaTeam(request);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Team);
    }

    @Test
    void testGetDettagliTeam() {
        TeamRequest request = new TeamRequest("Team Beta", "Details test", leader.getId());
        Team team = teamService.creaTeam(request);

        ResponseEntity<?> response = teamHandler.getDettagliTeam(team.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Team);
        Team retrievedTeam = (Team) response.getBody();
        assertEquals("Team Beta", retrievedTeam.getNome());
    }

    @Test
    void testGetDettagliTeamNotFound() {
        ResponseEntity<?> response = teamHandler.getDettagliTeam(99999L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testEsisteTeam() {
        TeamRequest request = new TeamRequest("Team Gamma", "Exists test", leader.getId());
        teamService.creaTeam(request);

        ResponseEntity<Boolean> existsResponse = teamHandler.esisteTeam("Team Gamma");
        ResponseEntity<Boolean> notExistsResponse = teamHandler.esisteTeam("NonEsistente");

        assertTrue(existsResponse.getBody());
        assertFalse(notExistsResponse.getBody());
    }
}
