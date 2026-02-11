package it.unicam.ids.builder;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneBuilderTest {

    @Test
    void testNewBuilder() {
        SottomissioneBuilder builder = SottomissioneBuilder.newBuilder();
        assertNotNull(builder);
    }

    @Test
    void testBuildWithAllFields() {
        DatiProgetto datiProgetto = new DatiProgetto("Titolo progetto", "Descrizione del progetto", "https://github.com/test/repo");

        Sottomissione sottomissione = SottomissioneBuilder.newBuilder()
                .datiProgetto(datiProgetto)
                .stato(StatoSottomissione.BOZZA)
                .teamId(1L)
                .hackathonId(2L)
                .build();

        assertNotNull(sottomissione);
        assertNotNull(sottomissione.getDatiProgetto());
        assertEquals("Descrizione del progetto", sottomissione.getDatiProgetto().getDescrizione());
        assertEquals("https://github.com/test/repo", sottomissione.getDatiProgetto().getLinkRepository());
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
        assertEquals(1L, sottomissione.getTeamId());
        assertEquals(2L, sottomissione.getHackathonId());
    }

    @Test
    void testBuildWithDefaultState() {
        DatiProgetto datiProgetto = new DatiProgetto("Titolo", "Progetto", "https://github.com");

        Sottomissione sottomissione = SottomissioneBuilder.newBuilder()
                .datiProgetto(datiProgetto)
                .build();

        assertNotNull(sottomissione);
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
    }

    @Test
    void testBuildWithConsegnataState() {
        DatiProgetto datiProgetto = new DatiProgetto("Titolo Finale", "Progetto Finale", "https://github.com/final");

        Sottomissione sottomissione = SottomissioneBuilder.newBuilder()
                .datiProgetto(datiProgetto)
                .stato(StatoSottomissione.CONSEGNATA)
                .build();

        assertNotNull(sottomissione);
        assertEquals(StatoSottomissione.CONSEGNATA, sottomissione.getStato());
        assertTrue(sottomissione.isConsegnata());
        assertFalse(sottomissione.isBozza());
    }

    @Test
    void testFluentApi() {
        DatiProgetto datiProgetto = new DatiProgetto("Titolo", "Desc", "https://link");
        SottomissioneBuilder builder = SottomissioneBuilder.newBuilder();

        // Verifica che ogni metodo restituisca il builder stesso
        assertSame(builder, builder.datiProgetto(datiProgetto));
        assertSame(builder, builder.stato(StatoSottomissione.BOZZA));
        assertSame(builder, builder.teamId(1L));
        assertSame(builder, builder.hackathonId(2L));
    }

    @Test
    void testBuildMinimalSottomissione() {
        Sottomissione sottomissione = SottomissioneBuilder.newBuilder().build();

        assertNotNull(sottomissione);
        assertNull(sottomissione.getDatiProgetto());
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
        assertNull(sottomissione.getTeamId());
        assertNull(sottomissione.getHackathonId());
    }

    @Test
    void testMultipleBuilds() {
        DatiProgetto datiProgetto = new DatiProgetto("Titolo", "Descrizione", "https://github.com");
        SottomissioneBuilder builder = SottomissioneBuilder.newBuilder()
                .datiProgetto(datiProgetto);

        Sottomissione s1 = builder.build();
        Sottomissione s2 = builder.build();

        assertNotSame(s1, s2);
        assertEquals(s1.getDatiProgetto().getDescrizione(), s2.getDatiProgetto().getDescrizione());
    }

    @Test
    void testDatiProgettoNull() {
        Sottomissione sottomissione = SottomissioneBuilder.newBuilder()
                .datiProgetto(null)
                .build();

        assertNotNull(sottomissione);
        assertNull(sottomissione.getDatiProgetto());
    }
}
