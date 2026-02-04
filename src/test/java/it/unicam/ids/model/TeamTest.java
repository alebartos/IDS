package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;
    private Utente leader;

    @BeforeEach
    void setUp() {
        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader.addRuolo(Ruolo.LEADER);

        team = new Team();
        team.setNome("Team Alpha");
        team.setDescrizione("Un team di sviluppatori");
        team.setDataCreazione(LocalDate.now());
        team.setLeader(leader);
    }

    @Test
    void testTeamCreation() {
        assertNotNull(team);
        assertEquals("Team Alpha", team.getNome());
        assertEquals("Un team di sviluppatori", team.getDescrizione());
        assertNotNull(team.getDataCreazione());
        assertNotNull(team.getLeader());
    }

    @Test
    void testGetMaxMembriTeam() {
        int maxMembri = team.getMaxMembriTeam();
        assertTrue(maxMembri >= 0);
    }

    @Test
    void testTeamLeaderRelationship() {
        assertEquals(leader, team.getLeader());
        assertEquals("Mario", team.getLeader().getNome());
    }
}
