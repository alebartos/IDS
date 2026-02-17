package it.unicam.ids.repository;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UtenteRepositoryTest {

    private UtenteRepository utenteRepository;

    @BeforeEach
    void setUp() {
        utenteRepository = new UtenteRepository();
    }

    @Test
    void testAdd() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        Utente saved = utenteRepository.add(utente);

        assertNotNull(saved.getId());
        assertEquals("Mario", saved.getNome());
    }

    @Test
    void testAddConIdEsistente() {
        Utente utente = new Utente("Mario", "Rossi", "mario@example.com", "password");
        utente.setId(50L);
        Utente saved = utenteRepository.add(utente);

        assertEquals(50L, saved.getId());
    }

    @Test
    void testModifyRecord() {
        Utente utente = utenteRepository.add(new Utente("Mario", "Rossi", "mario@example.com", "password"));
        utente.setNome("Mario Modificato");
        utenteRepository.modifyRecord(utente);

        Utente trovato = utenteRepository.findById(utente.getId()).orElseThrow();
        assertEquals("Mario Modificato", trovato.getNome());
    }

    @Test
    void testFindById() {
        Utente utente = utenteRepository.add(new Utente("Anna", "Bianchi", "anna@example.com", "pass"));
        assertTrue(utenteRepository.findById(utente.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(utenteRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindByEmail() {
        utenteRepository.add(new Utente("Luigi", "Verdi", "luigi@example.com", "pass"));
        Optional<Utente> trovato = utenteRepository.findByEmail("luigi@example.com");
        assertTrue(trovato.isPresent());
        assertEquals("Luigi", trovato.get().getNome());
    }

    @Test
    void testFindByEmailNonEsistente() {
        assertTrue(utenteRepository.findByEmail("nessuno@example.com").isEmpty());
    }

    @Test
    void testFindAll() {
        utenteRepository.add(new Utente("A", "A", "a@e.com", "p"));
        utenteRepository.add(new Utente("B", "B", "b@e.com", "p"));
        assertEquals(2, utenteRepository.findAll().size());
    }

    @Test
    void testFindByRuolo() {
        Utente org = new Utente("Org", "Test", "org@e.com", "p");
        org.getRuoli().add(Ruolo.ORGANIZZATORE);
        utenteRepository.add(org);

        Utente base = new Utente("Base", "Test", "base@e.com", "p");
        utenteRepository.add(base);

        List<Utente> organizzatori = utenteRepository.findByRuolo(Ruolo.ORGANIZZATORE);
        assertEquals(1, organizzatori.size());
    }

    @Test
    void testDeleteById() {
        Utente utente = utenteRepository.add(new Utente("X", "Y", "x@e.com", "p"));
        utenteRepository.deleteById(utente.getId());
        assertTrue(utenteRepository.findById(utente.getId()).isEmpty());
    }

    @Test
    void testDeleteAll() {
        utenteRepository.add(new Utente("A", "A", "a@e.com", "p"));
        utenteRepository.add(new Utente("B", "B", "b@e.com", "p"));
        utenteRepository.deleteAll();
        assertEquals(0, utenteRepository.count());
    }

    @Test
    void testExistsById() {
        Utente utente = utenteRepository.add(new Utente("M", "R", "m@e.com", "p"));
        assertTrue(utenteRepository.existsById(utente.getId()));
        assertFalse(utenteRepository.existsById(999L));
    }

    @Test
    void testExistsByEmail() {
        utenteRepository.add(new Utente("M", "R", "mario@test.com", "p"));
        assertTrue(utenteRepository.existsByEmail("mario@test.com"));
        assertFalse(utenteRepository.existsByEmail("nessuno@test.com"));
    }

    @Test
    void testCount() {
        assertEquals(0, utenteRepository.count());
        utenteRepository.add(new Utente("A", "A", "a@e.com", "p"));
        assertEquals(1, utenteRepository.count());
    }
}
