package it.unicam.ids.model;

import it.unicam.ids.dto.DatiProgetto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneTest {

    @Test
    void testCreazioneSottomissione() {
        Sottomissione sottomissione = new Sottomissione();

        assertNotNull(sottomissione);
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
        assertTrue(sottomissione.isBozza());
        assertFalse(sottomissione.isConsegnata());
        assertNotNull(sottomissione.getDataInvio());
    }

    @Test
    void testSettersAndGetters() {
        Sottomissione sottomissione = new Sottomissione();
        DatiProgetto datiProgetto = new DatiProgetto("Titolo progetto", "Descrizione del progetto", "https://github.com/test/repo");

        sottomissione.setId(1L);
        sottomissione.setDatiProgetto(datiProgetto);
        sottomissione.setTeamId(10L);
        sottomissione.setHackathonId(20L);

        assertEquals(1L, sottomissione.getId());
        assertNotNull(sottomissione.getDatiProgetto());
        assertEquals("Descrizione del progetto", sottomissione.getDatiProgetto().getDescrizione());
        assertEquals("https://github.com/test/repo", sottomissione.getDatiProgetto().getLinkRepository());
        assertEquals(10L, sottomissione.getTeamId());
        assertEquals(20L, sottomissione.getHackathonId());
        assertNotNull(sottomissione.getDataInvio());
    }

    @Test
    void testIsBozza() {
        Sottomissione sottomissione = new Sottomissione();

        assertTrue(sottomissione.isBozza());
        assertFalse(sottomissione.isConsegnata());

        sottomissione.setStato(StatoSottomissione.CONSEGNATA);

        assertFalse(sottomissione.isBozza());
        assertTrue(sottomissione.isConsegnata());
    }

    @Test
    void testDataInvioUpdates() {
        Sottomissione sottomissione = new Sottomissione();

        DatiProgetto datiProgetto = new DatiProgetto("Nuovo titolo", "Nuova descrizione", "https://github.com/new");
        sottomissione.setDatiProgetto(datiProgetto);
        assertEquals(LocalDate.now(), sottomissione.getDataInvio());

        sottomissione.setStato(StatoSottomissione.CONSEGNATA);
        assertEquals(LocalDate.now(), sottomissione.getDataInvio());
    }

    @Test
    void testAssociazioneTeamId() {
        Sottomissione sottomissione = new Sottomissione();

        sottomissione.setTeamId(5L);

        assertEquals(5L, sottomissione.getTeamId());
    }

    @Test
    void testAssociazioneHackathonId() {
        Sottomissione sottomissione = new Sottomissione();

        sottomissione.setHackathonId(10L);

        assertEquals(10L, sottomissione.getHackathonId());
    }

    @Test
    void testToString() {
        Sottomissione sottomissione = new Sottomissione();
        sottomissione.setId(1L);
        sottomissione.setDatiProgetto(new DatiProgetto("Test title", "Test description", "https://github.com"));
        sottomissione.setStato(StatoSottomissione.BOZZA);
        sottomissione.setTeamId(5L);
        sottomissione.setHackathonId(10L);

        String result = sottomissione.toString();

        assertTrue(result.contains("BOZZA"));
        assertTrue(result.contains("teamId=5"));
        assertTrue(result.contains("hackathonId=10"));
    }
}
