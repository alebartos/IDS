package it.unicam.ids.service;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private TeamService teamService;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private Utente leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        teamService = new TeamService(teamRepository, utenteRepository);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        TeamRequest request = new TeamRequest("Team Alpha", "Test team", leader.getId());

        Team team = teamService.creaTeam(request);

        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals("Team Alpha", team.getNome());
        assertEquals(leader.getId(), team.getLeaderId());
        // Verifica che il leader abbia il ruolo LEADER
        assertTrue(leader.hasRuolo(Ruolo.LEADER));
    }

    @Test
    void testCreaTeamDuplicateNome() {
        TeamRequest request1 = new TeamRequest("Team Duplicate", "First", leader.getId());
        teamService.creaTeam(request1);

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        leader2 = utenteRepository.save(leader2);

        TeamRequest request2 = new TeamRequest("Team Duplicate", "Second", leader2.getId());

        assertThrows(IllegalArgumentException.class, () -> teamService.creaTeam(request2));
    }

    @Test
    void testEsisteTeamConNome() {
        TeamRequest request = new TeamRequest("Team Beta", "Test", leader.getId());
        teamService.creaTeam(request);

        assertTrue(teamService.esisteTeamConNome("Team Beta"));
        assertFalse(teamService.esisteTeamConNome("Team NonEsistente"));
    }

    @Test
    void testValidaTeam() {
        Team validTeam = new Team();
        validTeam.setNome("Valid Team");
        validTeam.setLeaderId(leader.getId());

        assertTrue(teamService.validaTeam(validTeam));

        assertFalse(teamService.validaTeam(null));

        Team invalidTeam = new Team();
        invalidTeam.setNome("");
        invalidTeam.setLeaderId(leader.getId());
        assertFalse(teamService.validaTeam(invalidTeam));

        Team noLeaderTeam = new Team();
        noLeaderTeam.setNome("No Leader");
        assertFalse(teamService.validaTeam(noLeaderTeam));
    }

    @Test
    void testGetDettagliTeam() {
        TeamRequest request = new TeamRequest("Team Gamma", "Details test", leader.getId());
        Team createdTeam = teamService.creaTeam(request);

        Team retrievedTeam = teamService.getDettagliTeam(createdTeam.getId());

        assertNotNull(retrievedTeam);
        assertEquals(createdTeam.getId(), retrievedTeam.getId());
        assertEquals("Team Gamma", retrievedTeam.getNome());
    }

    @Test
    void testGetTeamByLeaderId() {
        TeamRequest request = new TeamRequest("Team Delta", "Leader test", leader.getId());
        Team createdTeam = teamService.creaTeam(request);

        Team retrievedTeam = teamService.getTeamByLeaderId(leader.getId());

        assertNotNull(retrievedTeam);
        assertEquals(createdTeam.getId(), retrievedTeam.getId());
    }

    @Test
    void testCreaTeamLeaderGiaEsistente() {
        TeamRequest request1 = new TeamRequest("Team One", "First", leader.getId());
        teamService.creaTeam(request1);

        TeamRequest request2 = new TeamRequest("Team Two", "Second", leader.getId());

        assertThrows(IllegalArgumentException.class, () -> teamService.creaTeam(request2));
    }

    @Test
    void testCreaTeamUtenteNonTrovato() {
        TeamRequest request = new TeamRequest("Team Invalid", "Test", 999L);

        assertThrows(IllegalArgumentException.class, () -> teamService.creaTeam(request));
    }
}
