package it.unicam.ids.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamRequestTest {

    @Test
    void testCostruttoreVuoto() {
        TeamRequest tr = new TeamRequest();
        assertNull(tr.getNome());
        assertNull(tr.getDescrizione());
        assertNull(tr.getLeaderId());
    }

    @Test
    void testCostruttoreConParametri() {
        TeamRequest tr = new TeamRequest("Team Alpha", "Descrizione team", 1L);
        assertEquals("Team Alpha", tr.getNome());
        assertEquals("Descrizione team", tr.getDescrizione());
        assertEquals(1L, tr.getLeaderId());
    }

    @Test
    void testSetters() {
        TeamRequest tr = new TeamRequest();
        tr.setNome("Team Beta");
        tr.setDescrizione("Desc");
        tr.setLeaderId(2L);

        assertEquals("Team Beta", tr.getNome());
        assertEquals("Desc", tr.getDescrizione());
        assertEquals(2L, tr.getLeaderId());
    }

    @Test
    void testEqualsStessoOggetto() {
        TeamRequest tr = new TeamRequest("Team", "Desc", 1L);
        assertEquals(tr, tr);
    }

    @Test
    void testEqualsOggettiUguali() {
        TeamRequest tr1 = new TeamRequest("Team", "Desc", 1L);
        TeamRequest tr2 = new TeamRequest("Team", "Desc", 1L);
        assertEquals(tr1, tr2);
    }

    @Test
    void testEqualsDiversi() {
        TeamRequest tr1 = new TeamRequest("Team A", "Desc", 1L);
        TeamRequest tr2 = new TeamRequest("Team B", "Desc", 2L);
        assertNotEquals(tr1, tr2);
    }

    @Test
    void testEqualsNull() {
        TeamRequest tr = new TeamRequest("Team", "Desc", 1L);
        assertNotEquals(null, tr);
    }

    @Test
    void testHashCodeCoerente() {
        TeamRequest tr1 = new TeamRequest("Team", "Desc", 1L);
        TeamRequest tr2 = new TeamRequest("Team", "Desc", 1L);
        assertEquals(tr1.hashCode(), tr2.hashCode());
    }

    @Test
    void testToString() {
        TeamRequest tr = new TeamRequest("Team Alpha", "Desc", 1L);
        String str = tr.toString();
        assertTrue(str.contains("Team Alpha"));
        assertTrue(str.contains("Desc"));
    }
}
