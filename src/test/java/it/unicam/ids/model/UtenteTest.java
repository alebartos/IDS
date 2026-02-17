package it.unicam.ids.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtenteTest {

    @Test
    void testCostruttoreVuoto() {
        Utente utente = new Utente();
        assertNull(utente.getId());
        assertNull(utente.getNome());
        assertNull(utente.getCognome());
        assertNull(utente.getEmail());
        assertNull(utente.getPassword());
        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
    }

    @Test
    void testCostruttoreConParametri() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals("mario@example.com", utente.getEmail());
        assertEquals("password", utente.getPassword());
        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
    }

    @Test
    void testGettersAndSetters() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setNome("Anna");
        utente.setCognome("Bianchi");
        utente.setEmail("anna@example.com");
        utente.setPassword("pass123");

        assertEquals(1L, utente.getId());
        assertEquals("Anna", utente.getNome());
        assertEquals("Bianchi", utente.getCognome());
        assertEquals("anna@example.com", utente.getEmail());
        assertEquals("pass123", utente.getPassword());
    }

    @Test
    void testSetRuoliMantieneBase() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        List<Ruolo> nuoviRuoli = new ArrayList<>();
        nuoviRuoli.add(Ruolo.ORGANIZZATORE);
        utente.setRuoli(nuoviRuoli);

        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
        assertTrue(utente.getRuoli().contains(Ruolo.ORGANIZZATORE));
    }

    @Test
    void testSetRuoliNull() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        utente.setRuoli(null);

        assertNotNull(utente.getRuoli());
        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
    }

    @Test
    void testEqualsStessoOggetto() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        utente.setId(1L);
        assertEquals(utente, utente);
    }

    @Test
    void testEqualsOggettiUguali() {
        Utente u1 = new Utente("Mario", "Rossi", "mario@example.com", "password");
        u1.setId(1L);
        Utente u2 = new Utente("Luigi", "Verdi", "mario@example.com", "pass2");
        u2.setId(1L);
        assertEquals(u1, u2);
    }

    @Test
    void testEqualsDiversi() {
        Utente u1 = new Utente("Mario", "Rossi", "mario@example.com", "password");
        u1.setId(1L);
        Utente u2 = new Utente("Anna", "Bianchi", "anna@example.com", "pass");
        u2.setId(2L);
        assertNotEquals(u1, u2);
    }

    @Test
    void testEqualsNull() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        assertNotEquals(null, utente);
    }

    @Test
    void testEqualsClasseDiversa() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        assertNotEquals("stringa", utente);
    }

    @Test
    void testHashCodeCoerente() {
        Utente u1 = new Utente("Mario", "Rossi", "mario@example.com", "password");
        u1.setId(1L);
        Utente u2 = new Utente("Luigi", "Verdi", "mario@example.com", "pass2");
        u2.setId(1L);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        utente.setId(1L);
        String str = utente.toString();
        assertTrue(str.contains("Mario"));
        assertTrue(str.contains("Rossi"));
        assertTrue(str.contains("mario@example.com"));
    }

    @Test
    void testAggiungiRuolo() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        utente.getRuoli().add(Ruolo.ORGANIZZATORE);
        assertTrue(utente.getRuoli().contains(Ruolo.ORGANIZZATORE));
        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
    }
}
