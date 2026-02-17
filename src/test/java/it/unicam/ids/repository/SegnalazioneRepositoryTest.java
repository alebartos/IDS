package it.unicam.ids.repository;

import it.unicam.ids.model.Segnalazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SegnalazioneRepositoryTest {

    private SegnalazioneRepository segnalazioneRepository;

    @BeforeEach
    void setUp() {
        segnalazioneRepository = new SegnalazioneRepository();
    }

    @Test
    void testAdd() {
        Segnalazione s = new Segnalazione("Problema grave", 1L, 1L);
        Segnalazione saved = segnalazioneRepository.add(s);

        assertNotNull(saved.getId());
        assertEquals("Problema grave", saved.getDescrizione());
    }

    @Test
    void testFindById() {
        Segnalazione s = segnalazioneRepository.add(new Segnalazione("Desc", 1L, 1L));
        Optional<Segnalazione> trovato = segnalazioneRepository.findById(s.getId());
        assertTrue(trovato.isPresent());
        assertEquals("Desc", trovato.get().getDescrizione());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(segnalazioneRepository.findById(999L).isEmpty());
    }

    @Test
    void testDeleteAll() {
        segnalazioneRepository.add(new Segnalazione("D1", 1L, 1L));
        segnalazioneRepository.add(new Segnalazione("D2", 2L, 1L));
        segnalazioneRepository.deleteAll();

        assertTrue(segnalazioneRepository.findById(1L).isEmpty());
        assertTrue(segnalazioneRepository.findById(2L).isEmpty());
    }
}
