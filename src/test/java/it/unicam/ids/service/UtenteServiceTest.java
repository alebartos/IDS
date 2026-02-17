package it.unicam.ids.service;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtenteServiceTest {

    private UtenteService utenteService;
    private UtenteRepository utenteRepository;

    @BeforeEach
    void setUp() {
        utenteRepository = new UtenteRepository();
        utenteService = new UtenteService(utenteRepository);
    }

    @Test
    void testRegistraUtenteSuccess() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "password");

        assertNotNull(utente);
        assertNotNull(utente.getId());
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals("mario@example.com", utente.getEmail());
        assertTrue(utente.getRuoli().contains(Ruolo.BASE));
    }

    @Test
    void testRegistraUtenteEmailDuplicata() {
        utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "password");

        assertThrows(IllegalArgumentException.class,
                () -> utenteService.registraUtente("Luigi", "Verdi", "mario@example.com", "pass2"));
    }

    @Test
    void testGetUtente() {
        Utente utente = utenteService.registraUtente("Anna", "Bianchi", "anna@example.com", "pass");
        Utente trovato = utenteService.getUtente(utente.getId());

        assertEquals(utente.getId(), trovato.getId());
        assertEquals("Anna", trovato.getNome());
    }

    @Test
    void testGetUtenteNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> utenteService.getUtente(999L));
    }

    @Test
    void testGetUtenteByEmail() {
        utenteService.registraUtente("Luigi", "Verdi", "luigi@example.com", "pass");
        Utente trovato = utenteService.getUtenteByEmail("luigi@example.com");

        assertEquals("Luigi", trovato.getNome());
    }

    @Test
    void testGetUtenteByEmailNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> utenteService.getUtenteByEmail("nessuno@example.com"));
    }

    @Test
    void testAddRuoloSuccess() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        Utente aggiornato = utenteService.addRuolo(utente.getId(), Ruolo.ORGANIZZATORE);

        assertTrue(aggiornato.getRuoli().contains(Ruolo.ORGANIZZATORE));
    }

    @Test
    void testAddRuoloGiaPresente() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        utenteService.addRuolo(utente.getId(), Ruolo.ORGANIZZATORE);

        assertThrows(IllegalArgumentException.class,
                () -> utenteService.addRuolo(utente.getId(), Ruolo.ORGANIZZATORE));
    }

    @Test
    void testDeleteRuoloSuccess() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        utenteService.addRuolo(utente.getId(), Ruolo.ORGANIZZATORE);
        Utente aggiornato = utenteService.deleteRuolo(utente.getId(), Ruolo.ORGANIZZATORE);

        assertFalse(aggiornato.getRuoli().contains(Ruolo.ORGANIZZATORE));
    }

    @Test
    void testDeleteRuoloNonPresente() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");

        assertThrows(IllegalArgumentException.class,
                () -> utenteService.deleteRuolo(utente.getId(), Ruolo.ORGANIZZATORE));
    }

    @Test
    void testDeleteRuoloBase() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");

        assertThrows(IllegalArgumentException.class,
                () -> utenteService.deleteRuolo(utente.getId(), Ruolo.BASE));
    }

    @Test
    void testCheckRuoloTrue() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        utenteService.addRuolo(utente.getId(), Ruolo.ORGANIZZATORE);

        assertTrue(utenteService.checkRuolo(utente.getId(), Ruolo.ORGANIZZATORE));
    }

    @Test
    void testCheckRuoloFalse() {
        Utente utente = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");

        assertFalse(utenteService.checkRuolo(utente.getId(), Ruolo.ORGANIZZATORE));
    }

    @Test
    void testGetUtentiByRuolo() {
        Utente u1 = utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        utenteService.addRuolo(u1.getId(), Ruolo.ORGANIZZATORE);

        Utente u2 = utenteService.registraUtente("Luigi", "Verdi", "luigi@example.com", "pass");
        utenteService.addRuolo(u2.getId(), Ruolo.ORGANIZZATORE);

        utenteService.registraUtente("Anna", "Bianchi", "anna@example.com", "pass");

        List<Utente> organizzatori = utenteService.getUtentiByRuolo(Ruolo.ORGANIZZATORE);
        assertEquals(2, organizzatori.size());
    }

    @Test
    void testGetAllUtenti() {
        utenteService.registraUtente("A", "A", "a@e.com", "p");
        utenteService.registraUtente("B", "B", "b@e.com", "p");
        utenteService.registraUtente("C", "C", "c@e.com", "p");

        List<Utente> tutti = utenteService.getAllUtenti();
        assertEquals(3, tutti.size());
    }

    @Test
    void testEsisteUtenteConEmailTrue() {
        utenteService.registraUtente("Mario", "Rossi", "mario@example.com", "pass");
        assertTrue(utenteService.esisteUtenteConEmail("mario@example.com"));
    }

    @Test
    void testEsisteUtenteConEmailFalse() {
        assertFalse(utenteService.esisteUtenteConEmail("nessuno@example.com"));
    }
}
