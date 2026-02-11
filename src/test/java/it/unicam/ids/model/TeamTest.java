package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setNome("Team Alpha");
        team.setLeaderId(1L);
    }

    @Test
    void testTeamCreation() {
        assertNotNull(team);
        assertEquals("Team Alpha", team.getNome());
        assertEquals(1L, team.getLeaderId());
        assertNotNull(team.getMembri());
        assertTrue(team.getMembri().isEmpty());
    }

    @Test
    void testTeamConstructor() {
        Team newTeam = new Team(1L, "Team Beta", 2L);

        assertEquals(1L, newTeam.getId());
        assertEquals("Team Beta", newTeam.getNome());
        assertEquals(2L, newTeam.getLeaderId());
        assertNotNull(newTeam.getMembri());
        assertTrue(newTeam.getMembri().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        team.setId(10L);
        team.setNome("Team Gamma");
        team.setLeaderId(5L);

        assertEquals(10L, team.getId());
        assertEquals("Team Gamma", team.getNome());
        assertEquals(5L, team.getLeaderId());
    }

    @Test
    void testAddMembro() {
        team.addMembro(100L);
        team.addMembro(101L);

        List<Long> membri = team.getMembri();
        assertEquals(2, membri.size());
        assertTrue(membri.contains(100L));
        assertTrue(membri.contains(101L));
    }

    @Test
    void testAddMembroDuplicato() {
        team.addMembro(100L);
        team.addMembro(100L);

        assertEquals(1, team.getMembri().size());
    }

    @Test
    void testAddMembroNull() {
        team.addMembro(null);

        assertTrue(team.getMembri().isEmpty());
    }

    @Test
    void testRimuoviMembro() {
        team.addMembro(100L);
        team.addMembro(101L);

        boolean removed = team.rimuoviMembro(100L);

        assertTrue(removed);
        assertEquals(1, team.getMembri().size());
        assertFalse(team.getMembri().contains(100L));
        assertTrue(team.getMembri().contains(101L));
    }

    @Test
    void testRimuoviMembroNonEsistente() {
        team.addMembro(100L);

        boolean removed = team.rimuoviMembro(999L);

        assertFalse(removed);
        assertEquals(1, team.getMembri().size());
    }

    @Test
    void testFindById() {
        team.addMembro(100L);
        team.addMembro(101L);

        assertTrue(team.findById(100L));
        assertTrue(team.findById(101L));
        assertFalse(team.findById(999L));
    }

    @Test
    void testGetSizeTeam() {
        assertEquals(0, team.getSizeTeam());

        team.addMembro(100L);
        assertEquals(1, team.getSizeTeam());

        team.addMembro(101L);
        assertEquals(2, team.getSizeTeam());

        team.rimuoviMembro(100L);
        assertEquals(1, team.getSizeTeam());
    }

    @Test
    void testGetMembriReturnsCopy() {
        team.addMembro(100L);

        List<Long> membri = team.getMembri();
        membri.add(999L);

        // La modifica della copia non deve influenzare la lista originale
        assertEquals(1, team.getMembri().size());
        assertFalse(team.getMembri().contains(999L));
    }

    @Test
    void testSetMembri() {
        List<Long> nuoviMembri = List.of(200L, 201L, 202L);
        team.setMembri(nuoviMembri);

        assertEquals(3, team.getMembri().size());
        assertTrue(team.findById(200L));
        assertTrue(team.findById(201L));
        assertTrue(team.findById(202L));
    }

    @Test
    void testSetMembriNull() {
        team.addMembro(100L);
        team.setMembri(null);

        assertNotNull(team.getMembri());
        assertTrue(team.getMembri().isEmpty());
    }

    @Test
    void testEquals() {
        Team team1 = new Team(1L, "Team Alpha", 1L);
        Team team2 = new Team(1L, "Team Alpha", 2L);
        Team team3 = new Team(2L, "Team Beta", 1L);

        assertEquals(team1, team2);
        assertNotEquals(team1, team3);
    }

    @Test
    void testHashCode() {
        Team team1 = new Team(1L, "Team Alpha", 1L);
        Team team2 = new Team(1L, "Team Alpha", 2L);

        assertEquals(team1.hashCode(), team2.hashCode());
    }

    @Test
    void testToString() {
        team.setId(1L);
        team.addMembro(100L);

        String result = team.toString();

        assertTrue(result.contains("Team Alpha"));
        assertTrue(result.contains("leaderId=1"));
        assertTrue(result.contains("membri="));
    }
}
