package it.unicam.ids.builder;

import it.unicam.ids.model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamBuilderTest {

    @Test
    void testBuildTeamSuccess() {
        Team team = TeamBuilder.newBuilder()
                .nome("Team Alpha")
                .leaderId(1L)
                .build();

        assertNotNull(team);
        assertEquals("Team Alpha", team.getNome());
        assertEquals(1L, team.getLeaderId());
    }

    @Test
    void testBuildTeamWithoutNome() {
        TeamBuilder builder = TeamBuilder.newBuilder()
                .leaderId(1L);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuildTeamWithoutLeaderId() {
        TeamBuilder builder = TeamBuilder.newBuilder()
                .nome("Team Alpha");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuilderChaining() {
        TeamBuilder builder = TeamBuilder.newBuilder();

        assertSame(builder, builder.nome("Test"));
        assertSame(builder, builder.leaderId(1L));
    }
}
