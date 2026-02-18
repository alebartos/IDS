package it.unicam.ids.repository;

import it.unicam.ids.model.RichiestaSupporto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class SupportoRepositoryTest {

    @Autowired
    private SupportoRepository supportoRepository;

    @Test
    void testAdd() {
        RichiestaSupporto r = new RichiestaSupporto("Problema", 1L, 1L);
        RichiestaSupporto saved = supportoRepository.save(r);

        assertNotNull(saved.getId());
        assertEquals("Problema", saved.getDescrizione());
    }

    @Test
    void testGetAllRichieste() {
        supportoRepository.save(new RichiestaSupporto("P1", 1L, 10L));
        supportoRepository.save(new RichiestaSupporto("P2", 2L, 10L));
        supportoRepository.save(new RichiestaSupporto("P3", 3L, 20L));

        List<RichiestaSupporto> lista = supportoRepository.findByHackathonId(10L);
        assertEquals(2, lista.size());
    }

    @Test
    void testGetAllRichiesteVuota() {
        List<RichiestaSupporto> lista = supportoRepository.findByHackathonId(999L);
        assertTrue(lista.isEmpty());
    }

    @Test
    void testFindById() {
        RichiestaSupporto r = supportoRepository.save(new RichiestaSupporto("P1", 1L, 1L));
        Optional<RichiestaSupporto> trovato = supportoRepository.findById(r.getId());
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(supportoRepository.findById(999L).isEmpty());
    }

    @Test
    void testModifyRecord() {
        RichiestaSupporto r = supportoRepository.save(new RichiestaSupporto("P1", 1L, 1L));
        r.setRisolta(true);
        supportoRepository.save(r);

        RichiestaSupporto aggiornata = supportoRepository.findById(r.getId()).orElseThrow();
        assertTrue(aggiornata.isRisolta());
    }

    @Test
    void testDeleteAll() {
        supportoRepository.save(new RichiestaSupporto("P1", 1L, 1L));
        supportoRepository.save(new RichiestaSupporto("P2", 2L, 1L));
        supportoRepository.deleteAll();

        assertTrue(supportoRepository.findByHackathonId(1L).isEmpty());
    }
}
