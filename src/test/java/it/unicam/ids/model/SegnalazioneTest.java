package it.unicam.ids.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegnalazioneTest {

    @Test
    void testCostruttoreDefault() {
        Segnalazione segnalazione = new Segnalazione();

        assertFalse(segnalazione.isGestita());
    }

    @Test
    void testCostruttoreConParametri() {
        Segnalazione segnalazione = new Segnalazione("Comportamento scorretto", 1L, 10L);

        assertEquals("Comportamento scorretto", segnalazione.getDescrizione());
        assertEquals(1L, segnalazione.getTeamId());
        assertEquals(10L, segnalazione.getHackathonId());
        assertFalse(segnalazione.isGestita());
    }

    @Test
    void testSettersGetters() {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setId(1L);
        segnalazione.setDescrizione("Test");
        segnalazione.setTeamId(5L);
        segnalazione.setHackathonId(10L);
        segnalazione.setGestita(true);

        assertEquals(1L, segnalazione.getId());
        assertEquals("Test", segnalazione.getDescrizione());
        assertEquals(5L, segnalazione.getTeamId());
        assertEquals(10L, segnalazione.getHackathonId());
        assertTrue(segnalazione.isGestita());
    }

    @Test
    void testEquals() {
        Segnalazione s1 = new Segnalazione("Desc1", 1L, 10L);
        s1.setId(1L);
        Segnalazione s2 = new Segnalazione("Desc2", 2L, 20L);
        s2.setId(1L);

        assertEquals(s1, s2);
    }

    @Test
    void testNotEquals() {
        Segnalazione s1 = new Segnalazione("Desc1", 1L, 10L);
        s1.setId(1L);
        Segnalazione s2 = new Segnalazione("Desc2", 2L, 20L);
        s2.setId(2L);

        assertNotEquals(s1, s2);
    }

    @Test
    void testHashCode() {
        Segnalazione s1 = new Segnalazione();
        s1.setId(1L);
        Segnalazione s2 = new Segnalazione();
        s2.setId(1L);

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToString() {
        Segnalazione segnalazione = new Segnalazione("Problema", 1L, 10L);
        segnalazione.setId(1L);

        String str = segnalazione.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("Problema"));
        assertTrue(str.contains("teamId=1"));
        assertTrue(str.contains("hackathonId=10"));
        assertTrue(str.contains("gestita=false"));
    }
}
