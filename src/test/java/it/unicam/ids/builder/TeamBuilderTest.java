package it.unicam.ids.builder;

import it.unicam.ids.model.Leader;
import it.unicam.ids.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamBuilderTest {

    private Leader leader;

    @BeforeEach
    void setUp() {
        leader = new Leader("Mario", "Rossi", "mario.rossi@example.com", "password123");
    }

    @Test
    void testBuildTeamSuccess() {
        Team team = TeamBuilder.newBuilder()
                .nome("Team Alpha")
                .descrizione("Test team")
                .leader(leader)
                .build();

        assertNotNull(team);
        assertEquals("Team Alpha", team.getNome());
        assertEquals("Test team", team.getDescrizione());
        assertEquals(leader, team.getLeader());
        assertNotNull(team.getDataCreazione());
    }

    @Test
    void testBuildTeamWithoutNome() {
        TeamBuilder builder = TeamBuilder.newBuilder()
                .descrizione("Test team")
                .leader(leader);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuildTeamWithoutLeader() {
        TeamBuilder builder = TeamBuilder.newBuilder()
                .nome("Team Alpha")
                .descrizione("Test team");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuilderChaining() {
        TeamBuilder builder = TeamBuilder.newBuilder();

        assertSame(builder, builder.nome("Test"));
        assertSame(builder, builder.descrizione("Description"));
        assertSame(builder, builder.leader(leader));
    }

    @Test
    void testCustomDataCreazione() {
        LocalDate customDate = LocalDate.of(2025, 1, 1);

        Team team = TeamBuilder.newBuilder()
                .nome("Team Beta")
                .descrizione("Test")
                .leader(leader)
                .dataCreazione(customDate)
                .build();

        assertEquals(customDate, team.getDataCreazione());
    }
}
