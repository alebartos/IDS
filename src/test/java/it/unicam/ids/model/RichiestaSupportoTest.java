package it.unicam.ids.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RichiestaSupportoTest {

    @Test
    void testCostruttoreDefault() {
        RichiestaSupporto richiesta = new RichiestaSupporto();

        assertNotNull(richiesta.getData());
        assertEquals(LocalDate.now(), richiesta.getData());
        assertFalse(richiesta.isRisolta());
    }

    @Test
    void testCostruttoreConParametri() {
        RichiestaSupporto richiesta = new RichiestaSupporto("Problema tecnico", 1L, 10L);

        assertEquals("Problema tecnico", richiesta.getDescrizione());
        assertEquals(1L, richiesta.getTeamId());
        assertEquals(10L, richiesta.getHackathonId());
        assertEquals(LocalDate.now(), richiesta.getData());
        assertFalse(richiesta.isRisolta());
    }

    @Test
    void testSettersGetters() {
        RichiestaSupporto richiesta = new RichiestaSupporto();
        richiesta.setId(1L);
        richiesta.setDescrizione("Test descrizione");
        richiesta.setTeamId(5L);
        richiesta.setHackathonId(10L);
        richiesta.setData(LocalDate.of(2025, 6, 15));
        richiesta.setRisolta(true);

        assertEquals(1L, richiesta.getId());
        assertEquals("Test descrizione", richiesta.getDescrizione());
        assertEquals(5L, richiesta.getTeamId());
        assertEquals(10L, richiesta.getHackathonId());
        assertEquals(LocalDate.of(2025, 6, 15), richiesta.getData());
        assertTrue(richiesta.isRisolta());
    }

    @Test
    void testEquals() {
        RichiestaSupporto r1 = new RichiestaSupporto("Desc1", 1L, 10L);
        r1.setId(1L);
        RichiestaSupporto r2 = new RichiestaSupporto("Desc2", 2L, 20L);
        r2.setId(1L);

        assertEquals(r1, r2);
    }

    @Test
    void testNotEquals() {
        RichiestaSupporto r1 = new RichiestaSupporto("Desc1", 1L, 10L);
        r1.setId(1L);
        RichiestaSupporto r2 = new RichiestaSupporto("Desc2", 2L, 20L);
        r2.setId(2L);

        assertNotEquals(r1, r2);
    }

    @Test
    void testHashCode() {
        RichiestaSupporto r1 = new RichiestaSupporto();
        r1.setId(1L);
        RichiestaSupporto r2 = new RichiestaSupporto();
        r2.setId(1L);

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString() {
        RichiestaSupporto richiesta = new RichiestaSupporto("Problema", 1L, 10L);
        richiesta.setId(1L);

        String str = richiesta.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("Problema"));
        assertTrue(str.contains("teamId=1"));
        assertTrue(str.contains("hackathonId=10"));
        assertTrue(str.contains("risolta=false"));
    }
}
