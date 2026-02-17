package it.unicam.ids.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatiProgettoTest {

    @Test
    void testCostruttoreVuoto() {
        DatiProgetto dp = new DatiProgetto();
        assertNull(dp.getTitolo());
        assertNull(dp.getDescrizione());
        assertNull(dp.getLinkRepository());
    }

    @Test
    void testCostruttoreConParametri() {
        DatiProgetto dp = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");
        assertEquals("Titolo", dp.getTitolo());
        assertEquals("Desc", dp.getDescrizione());
        assertEquals("https://github.com/repo", dp.getLinkRepository());
    }

    @Test
    void testSetters() {
        DatiProgetto dp = new DatiProgetto();
        dp.setTitolo("Nuovo Titolo");
        dp.setDescrizione("Nuova Desc");
        dp.setLinkRepository("https://github.com/nuovo");

        assertEquals("Nuovo Titolo", dp.getTitolo());
        assertEquals("Nuova Desc", dp.getDescrizione());
        assertEquals("https://github.com/nuovo", dp.getLinkRepository());
    }
}
