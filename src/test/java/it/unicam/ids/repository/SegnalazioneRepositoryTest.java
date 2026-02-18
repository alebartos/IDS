package it.unicam.ids.repository;

import it.unicam.ids.model.Segnalazione;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class SegnalazioneRepositoryTest {

    @Autowired
    private SegnalazioneRepository segnalazioneRepository;

    @Test
    void testAdd() {
        Segnalazione s = new Segnalazione("Problema grave", 1L, 1L);
        Segnalazione saved = segnalazioneRepository.save(s);

        assertNotNull(saved.getId());
        assertEquals("Problema grave", saved.getDescrizione());
    }

    @Test
    void testFindById() {
        Segnalazione s = segnalazioneRepository.save(new Segnalazione("Desc", 1L, 1L));
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
        segnalazioneRepository.save(new Segnalazione("D1", 1L, 1L));
        segnalazioneRepository.save(new Segnalazione("D2", 2L, 1L));
        segnalazioneRepository.deleteAll();

        assertEquals(0, segnalazioneRepository.count());
    }
}
