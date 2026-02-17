package it.unicam.ids.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HackathonRequestTest {

    @Test
    void testCostruttoreVuoto() {
        HackathonRequest hr = new HackathonRequest();
        assertNull(hr.getNome());
        assertNull(hr.getDescrizione());
        assertNull(hr.getDataInizio());
        assertNull(hr.getDataFine());
        assertNull(hr.getScadenzaIscrizioni());
        assertNull(hr.getLuogo());
        assertNull(hr.getRegolamento());
        assertNull(hr.getMaxMembriTeam());
    }

    @Test
    void testCostruttoreConParametri() {
        LocalDate inizio = LocalDate.of(2025, 3, 1);
        LocalDate fine = LocalDate.of(2025, 3, 5);
        LocalDate scadenza = LocalDate.of(2025, 2, 20);

        HackathonRequest hr = new HackathonRequest("Hack", "Desc", inizio, fine,
                scadenza, "Roma", "Regole", 5000.0, 5);

        assertEquals("Hack", hr.getNome());
        assertEquals("Desc", hr.getDescrizione());
        assertEquals(inizio, hr.getDataInizio());
        assertEquals(fine, hr.getDataFine());
        assertEquals(scadenza, hr.getScadenzaIscrizioni());
        assertEquals("Roma", hr.getLuogo());
        assertEquals("Regole", hr.getRegolamento());
        assertEquals(5000.0, hr.getPremio());
        assertEquals(5, hr.getMaxMembriTeam());
    }

    @Test
    void testSetters() {
        HackathonRequest hr = new HackathonRequest();
        LocalDate inizio = LocalDate.of(2025, 4, 1);
        LocalDate fine = LocalDate.of(2025, 4, 5);

        hr.setNome("Nuovo");
        hr.setDescrizione("Nuova Desc");
        hr.setDataInizio(inizio);
        hr.setDataFine(fine);
        hr.setScadenzaIscrizioni(LocalDate.of(2025, 3, 25));
        hr.setLuogo("Milano");
        hr.setRegolamento("Nuove regole");
        hr.setPremio(10000.0);
        hr.setMaxMembriTeam(10);

        assertEquals("Nuovo", hr.getNome());
        assertEquals("Nuova Desc", hr.getDescrizione());
        assertEquals(inizio, hr.getDataInizio());
        assertEquals(fine, hr.getDataFine());
        assertEquals("Milano", hr.getLuogo());
        assertEquals("Nuove regole", hr.getRegolamento());
        assertEquals(10000.0, hr.getPremio());
        assertEquals(10, hr.getMaxMembriTeam());
    }

    @Test
    void testEqualsStessoOggetto() {
        HackathonRequest hr = new HackathonRequest("Hack", "Desc",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        assertEquals(hr, hr);
    }

    @Test
    void testEqualsOggettiUguali() {
        HackathonRequest hr1 = new HackathonRequest("Hack", "Desc1",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        HackathonRequest hr2 = new HackathonRequest("Hack", "Desc2",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        assertEquals(hr1, hr2);
    }

    @Test
    void testEqualsDiversi() {
        HackathonRequest hr1 = new HackathonRequest("Hack A", "Desc",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        HackathonRequest hr2 = new HackathonRequest("Hack B", "Desc",
                LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 5),
                null, null, null, 0, null);
        assertNotEquals(hr1, hr2);
    }

    @Test
    void testHashCodeCoerente() {
        HackathonRequest hr1 = new HackathonRequest("Hack", "Desc",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        HackathonRequest hr2 = new HackathonRequest("Hack", "Desc",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, null, null, 0, null);
        assertEquals(hr1.hashCode(), hr2.hashCode());
    }

    @Test
    void testToString() {
        HackathonRequest hr = new HackathonRequest("Hack Test", "Desc",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 5),
                null, "Roma", null, 0, null);
        String str = hr.toString();
        assertTrue(str.contains("Hack Test"));
        assertTrue(str.contains("Roma"));
    }
}
