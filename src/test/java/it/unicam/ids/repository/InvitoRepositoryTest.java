package it.unicam.ids.repository;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InvitoRepositoryTest {

    private InvitoRepository invitoRepository;

    @BeforeEach
    void setUp() {
        invitoRepository = new InvitoRepository();
    }

    @Test
    void testAdd() {
        Invito invito = new Invito(1L, 2L);
        Invito saved = invitoRepository.add(invito);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getTeamId());
        assertEquals(2L, saved.getDestinatario());
    }

    @Test
    void testModifyRecord() {
        Invito invito = invitoRepository.add(new Invito(1L, 2L));
        invito.setStato(StatoInvito.ACCETTATO);
        invitoRepository.modifyRecord(invito);

        Invito trovato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.ACCETTATO, trovato.getStato());
    }

    @Test
    void testFindById() {
        Invito invito = invitoRepository.add(new Invito(1L, 2L));
        assertTrue(invitoRepository.findById(invito.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(invitoRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        invitoRepository.add(new Invito(1L, 2L));
        invitoRepository.add(new Invito(3L, 4L));
        assertEquals(2, invitoRepository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        invitoRepository.add(new Invito(1L, 2L));
        invitoRepository.deleteAll();
        assertEquals(0, invitoRepository.count());
    }

    @Test
    void testDeleteById() {
        Invito invito = invitoRepository.add(new Invito(1L, 2L));
        invitoRepository.deleteById(invito.getId());
        assertTrue(invitoRepository.findById(invito.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Invito invito = invitoRepository.add(new Invito(1L, 2L));
        assertTrue(invitoRepository.existsById(invito.getId()));
        assertFalse(invitoRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, invitoRepository.count());
        invitoRepository.add(new Invito(1L, 2L));
        assertEquals(1, invitoRepository.count());
    }

    @Test
    void testFindByTeamAndDestinatario() {
        invitoRepository.add(new Invito(1L, 2L));
        Optional<Invito> trovato = invitoRepository.findByTeamAndDestinatario(1L, 2L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByTeamAndDestinatarioNonInAttesa() {
        Invito invito = invitoRepository.add(new Invito(1L, 2L));
        invito.setStato(StatoInvito.RIFIUTATO);
        invitoRepository.modifyRecord(invito);

        Optional<Invito> trovato = invitoRepository.findByTeamAndDestinatario(1L, 2L);
        assertTrue(trovato.isEmpty());
    }

    @Test
    void testFindByDestinatarioId() {
        invitoRepository.add(new Invito(1L, 5L));
        invitoRepository.add(new Invito(2L, 5L));
        invitoRepository.add(new Invito(3L, 6L));

        List<Invito> inviti = invitoRepository.findByDestinatarioId(5L);
        assertEquals(2, inviti.size());
    }

    @Test
    void testFindByTeamId() {
        invitoRepository.add(new Invito(1L, 2L));
        invitoRepository.add(new Invito(1L, 3L));
        invitoRepository.add(new Invito(2L, 4L));

        List<Invito> inviti = invitoRepository.findByTeamId(1L);
        assertEquals(2, inviti.size());
    }

    @Test
    void testFindPendingByDestinatarioId() {
        invitoRepository.add(new Invito(1L, 5L));
        Invito i2 = invitoRepository.add(new Invito(2L, 5L));
        i2.setStato(StatoInvito.RIFIUTATO);
        invitoRepository.modifyRecord(i2);

        List<Invito> pending = invitoRepository.findPendingByDestinatarioId(5L);
        assertEquals(1, pending.size());
    }

    @Test
    void testRifiutaAltriInviti() {
        Invito i1 = invitoRepository.add(new Invito(1L, 5L));
        Invito i2 = invitoRepository.add(new Invito(2L, 5L));
        Invito i3 = invitoRepository.add(new Invito(3L, 5L));

        invitoRepository.rifiutaAltriInviti(5L, i1.getId());

        assertEquals(StatoInvito.IN_ATTESA, invitoRepository.findById(i1.getId()).orElseThrow().getStato());
        assertEquals(StatoInvito.RIFIUTATO, invitoRepository.findById(i2.getId()).orElseThrow().getStato());
        assertEquals(StatoInvito.RIFIUTATO, invitoRepository.findById(i3.getId()).orElseThrow().getStato());
    }
}
