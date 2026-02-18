package it.unicam.ids.repository;

import com.ids.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TeamRepositoryTest {

    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
    }

    @Test
    void testAdd() {
        Team team = new Team();
        team.setNome("Team A");
        team.setLeaderId(1L);
        Team saved = teamRepository.add(team);

        assertNotNull(saved.getId());
        assertEquals("Team A", saved.getNome());
    }

    @Test
    void testAddConIdEsistente() {
        Team team = new Team(10L, "Team B", 1L);
        Team saved = teamRepository.add(team);

        assertEquals(10L, saved.getId());
    }

    @Test
    void testModifyRecord() {
        Team team = teamRepository.add(new Team(null, "Team C", 1L));
        team.setNome("Team C Modificato");
        teamRepository.modifyRecord(team);

        Team trovato = teamRepository.findById(team.getId()).orElseThrow();
        assertEquals("Team C Modificato", trovato.getNome());
    }

    @Test
    void testFindById() {
        Team team = teamRepository.add(new Team(null, "Team D", 1L));
        Optional<Team> trovato = teamRepository.findById(team.getId());
        assertTrue(trovato.isPresent());
        assertEquals("Team D", trovato.get().getNome());
    }

    @Test
    void testFindByIdNonEsistente() {
        Optional<Team> trovato = teamRepository.findById(999L);
        assertTrue(trovato.isEmpty());
    }

    @Test
    void testFindByName() {
        teamRepository.add(new Team(null, "Team E", 1L));
        Optional<Team> trovato = teamRepository.findByName("Team E");
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByNameNonEsistente() {
        Optional<Team> trovato = teamRepository.findByName("Inesistente");
        assertTrue(trovato.isEmpty());
    }

    @Test
    void testFindByUtenteIdLeader() {
        Team team = new Team(null, "Team F", 5L);
        teamRepository.add(team);
        Optional<Team> trovato = teamRepository.findByUtenteId(5L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByUtenteIdMembro() {
        Team team = new Team(null, "Team G", 1L);
        team = teamRepository.add(team);
        team.getMembri().add(10L);
        teamRepository.modifyRecord(team);

        Optional<Team> trovato = teamRepository.findByUtenteId(10L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindAll() {
        teamRepository.add(new Team(null, "Team 1", 1L));
        teamRepository.add(new Team(null, "Team 2", 2L));
        assertEquals(2, teamRepository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        teamRepository.add(new Team(null, "Team 1", 1L));
        teamRepository.add(new Team(null, "Team 2", 2L));
        teamRepository.deleteAll();
        assertEquals(0, teamRepository.count());
    }

    @Test
    void testDeleteById() {
        Team team = teamRepository.add(new Team(null, "Team X", 1L));
        teamRepository.deleteById(team.getId());
        assertTrue(teamRepository.findById(team.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Team team = teamRepository.add(new Team(null, "Team Y", 1L));
        assertTrue(teamRepository.existsById(team.getId()));
        assertFalse(teamRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, teamRepository.count());
        teamRepository.add(new Team(null, "T1", 1L));
        assertEquals(1, teamRepository.count());
    }

    @Test
    void testFindByLeaderId() {
        teamRepository.add(new Team(null, "Team Leader", 42L));
        Optional<Team> trovato = teamRepository.findByLeaderId(42L);
        assertTrue(trovato.isPresent());
        assertEquals("Team Leader", trovato.get().getNome());
    }

    @Test
    void testFindByLeaderIdNonEsistente() {
        Optional<Team> trovato = teamRepository.findByLeaderId(999L);
        assertTrue(trovato.isEmpty());
    }
}
