package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LeaderTest {

    private Leader leader;

    @BeforeEach
    void setUp() {
        leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
    }

    @Test
    void testLeaderCreation() {
        assertNotNull(leader);
        assertEquals("Mario", leader.getNome());
        assertEquals("Rossi", leader.getCognome());
        assertEquals("mario.rossi@example.com", leader.getEmail());
        assertEquals("password123", leader.getPassword());
    }

    @Test
    void testLeaderTeamRelationship() {
        Team team = new Team();
        team.setNome("Team Alpha");
        team.setDescrizione("Test team");
        team.setDataCreazione(LocalDate.now());
        team.setLeader(leader);

        leader.setTeam(team);

        assertNotNull(leader.getTeam());
        assertEquals("Team Alpha", leader.getTeam().getNome());
    }
}
