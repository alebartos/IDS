package it.unicam.ids.service;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.LeaderRepository;
import it.unicam.ids.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private TeamService teamService;
    private TeamRepository teamRepository;
    private LeaderRepository leaderRepository;
    private Leader leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        leaderRepository = new LeaderRepository();
        teamService = new TeamService(teamRepository, leaderRepository);

        leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = leaderRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        TeamRequest request = new TeamRequest("Team Alpha", "Test team", leader.getId());

        Team team = teamService.creaTeam(request);

        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals("Team Alpha", team.getNome());
        assertEquals("Test team", team.getDescrizione());
        assertEquals(leader.getId(), team.getLeader().getId());
    }

    @Test
    void testCreaTeamDuplicateNome() {
        TeamRequest request1 = new TeamRequest("Team Duplicate", "First", leader.getId());
        teamService.creaTeam(request1);

        Leader leader2 = new Leader("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        leader2 = leaderRepository.save(leader2);

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
        validTeam.setDataCreazione(LocalDate.now());
        validTeam.setLeader(leader);

        assertTrue(teamService.validaTeam(validTeam));

        assertFalse(teamService.validaTeam(null));

        Team invalidTeam = new Team();
        invalidTeam.setNome("");
        invalidTeam.setDataCreazione(LocalDate.now());
        invalidTeam.setLeader(leader);
        assertFalse(teamService.validaTeam(invalidTeam));
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
    void testGetTeamByLeader() {
        TeamRequest request = new TeamRequest("Team Delta", "Leader test", leader.getId());
        Team createdTeam = teamService.creaTeam(request);

        Team retrievedTeam = teamService.getTeamByLeader(leader);

        assertNotNull(retrievedTeam);
        assertEquals(createdTeam.getId(), retrievedTeam.getId());
    }
}
