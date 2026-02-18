package it.unicam.ids.builder;

import it.unicam.ids.model.Hackathon;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HackathonBuilderTest {

    @Test
    void testBuildHackathonSuccess() {
        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome("Hackathon 2025")
                .descrizione("Test event")
                .dataInizio(LocalDate.of(2025, 3, 1))
                .dataFine(LocalDate.of(2025, 3, 3))
                .scadenzaIscrizioni(LocalDate.of(2025, 2, 15))
                .luogo("Milano")
                .regolamento("Rules")
                .premio("1000.0")
                .maxMembriTeam(6)
                .build();

        assertNotNull(hackathon);
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals("Test event", hackathon.getDescrizione());
        assertEquals("Milano", hackathon.getLuogo());
        assertEquals(1000.0, hackathon.getPremio());
        assertEquals(6, hackathon.getMaxMembriTeam());
    }

    @Test
    void testBuildHackathonWithoutNome() {
        HackathonBuilder builder = HackathonBuilder.newBuilder()
                .dataInizio(LocalDate.of(2025, 3, 1))
                .dataFine(LocalDate.of(2025, 3, 3))
                .scadenzaIscrizioni(LocalDate.of(2025, 2, 15))
                .luogo("Milano");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuildHackathonWithoutDataInizio() {
        HackathonBuilder builder = HackathonBuilder.newBuilder()
                .nome("Hackathon")
                .dataFine(LocalDate.of(2025, 3, 3))
                .scadenzaIscrizioni(LocalDate.of(2025, 2, 15))
                .luogo("Milano");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void testBuildHackathonWithoutLuogo() {
        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome("Hackathon")
                .dataInizio(LocalDate.of(2025, 3, 1))
                .dataFine(LocalDate.of(2025, 3, 3))
                .build();

        assertNotNull(hackathon);
        assertNull(hackathon.getLuogo());
    }

    @Test
    void testBuilderChaining() {
        HackathonBuilder builder = HackathonBuilder.newBuilder();

        assertSame(builder, builder.nome("Test"));
        assertSame(builder, builder.descrizione("Description"));
        assertSame(builder, builder.luogo("Location"));
    }

    @Test
    void testDefaultMaxMembriTeam() {
        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome("Hackathon 2025")
                .dataInizio(LocalDate.of(2025, 3, 1))
                .dataFine(LocalDate.of(2025, 3, 3))
                .scadenzaIscrizioni(LocalDate.of(2025, 2, 15))
                .luogo("Milano")
                .build();

        assertEquals(5, hackathon.getMaxMembriTeam());
    }
}
