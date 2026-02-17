package it.unicam.ids.repository;

import it.unicam.ids.model.RichiestaSupporto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SupportoRepositoryTest {

    private SupportoRepository supportoRepository;

    @BeforeEach
    void setUp() {
        supportoRepository = new SupportoRepository();
    }

    @Test
    void testAdd() {
        RichiestaSupporto r = new RichiestaSupporto("Problema", 1L, 1L);
        RichiestaSupporto saved = supportoRepository.add(r);

        assertNotNull(saved.getId());
        assertEquals("Problema", saved.getDescrizione());
    }

    @Test
    void testGetAllRichieste() {
        supportoRepository.add(new RichiestaSupporto("P1", 1L, 10L));
        supportoRepository.add(new RichiestaSupporto("P2", 2L, 10L));
        supportoRepository.add(new RichiestaSupporto("P3", 3L, 20L));

        List<RichiestaSupporto> lista = supportoRepository.getAllRichieste(10L);
        assertEquals(2, lista.size());
    }

    @Test
    void testGetAllRichiesteVuota() {
        List<RichiestaSupporto> lista = supportoRepository.getAllRichieste(999L);
        assertTrue(lista.isEmpty());
    }

    @Test
    void testFindById() {
        RichiestaSupporto r = supportoRepository.add(new RichiestaSupporto("P1", 1L, 1L));
        Optional<RichiestaSupporto> trovato = supportoRepository.findById(r.getId());
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(supportoRepository.findById(999L).isEmpty());
    }

    @Test
    void testModifyRecord() {
        RichiestaSupporto r = supportoRepository.add(new RichiestaSupporto("P1", 1L, 1L));
        r.setRisolta(true);
        supportoRepository.modifyRecord(r);

        RichiestaSupporto aggiornata = supportoRepository.findById(r.getId()).orElseThrow();
        assertTrue(aggiornata.isRisolta());
    }

    @Test
    void testDeleteAll() {
        supportoRepository.add(new RichiestaSupporto("P1", 1L, 1L));
        supportoRepository.add(new RichiestaSupporto("P2", 2L, 1L));
        supportoRepository.deleteAll();

        assertTrue(supportoRepository.getAllRichieste(1L).isEmpty());
    }
}
