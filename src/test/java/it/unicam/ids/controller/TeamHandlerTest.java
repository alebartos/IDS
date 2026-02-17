package it.unicam.ids.controller;

import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamHandlerTest {

    private TeamHandler teamHandler;
    private TeamService teamService;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private InvitoRepository invitoRepository;
    private Utente leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        invitoRepository = new InvitoRepository();
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);
        teamHandler = new TeamHandler(teamService);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.add(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        Result<Team> response = teamHandler.creaTeam("Team Alpha", leader.getId());

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("Team Alpha", response.getData().getNome());
    }
}
