package it.unicam.ids.builder;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvitoBuilderTest {

    @Test
    void testBuildDefault() {
        Invito invito = InvitoBuilder.newBuilder()
                .team(1L)
                .destinatario(2L)
                .build();

        assertEquals(1L, invito.getTeamId());
        assertEquals(2L, invito.getDestinatario());
        assertEquals(LocalDate.now(), invito.getDataInvio());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testBuildConTuttiICampi() {
        LocalDate data = LocalDate.of(2025, 6, 15);
        Invito invito = InvitoBuilder.newBuilder()
                .team(5L)
                .destinatario(10L)
                .dataInvio(data)
                .stato(StatoInvito.ACCETTATO)
                .build();

        assertEquals(5L, invito.getTeamId());
        assertEquals(10L, invito.getDestinatario());
        assertEquals(data, invito.getDataInvio());
        assertEquals(StatoInvito.ACCETTATO, invito.getStato());
    }

    @Test
    void testBuildSenzaParametri() {
        Invito invito = InvitoBuilder.newBuilder().build();

        assertNull(invito.getTeamId());
        assertNull(invito.getDestinatario());
        assertEquals(LocalDate.now(), invito.getDataInvio());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testBuilderFluency() {
        InvitoBuilder builder = InvitoBuilder.newBuilder();

        assertSame(builder, builder.team(1L));
        assertSame(builder, builder.destinatario(2L));
        assertSame(builder, builder.dataInvio(LocalDate.now()));
        assertSame(builder, builder.stato(StatoInvito.RIFIUTATO));
    }
}
