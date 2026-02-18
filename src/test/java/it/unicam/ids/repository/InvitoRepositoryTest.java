package it.unicam.ids.repository;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class InvitoRepositoryTest {

    @Autowired
    private InvitoRepository invitoRepository;

    @Test
    void testAdd() {
        Invito invito = new Invito(1L, 2L);
        Invito saved = invitoRepository.save(invito);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getTeamId());
        assertEquals(2L, saved.getDestinatario());
    }

    @Test
    void testModifyRecord() {
        Invito invito = invitoRepository.save(new Invito(1L, 2L));
        invito.setStato(StatoInvito.ACCETTATO);
        invitoRepository.save(invito);

        Invito trovato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.ACCETTATO, trovato.getStato());
    }

    @Test
    void testFindById() {
        Invito invito = invitoRepository.save(new Invito(1L, 2L));
        assertTrue(invitoRepository.findById(invito.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(invitoRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        invitoRepository.save(new Invito(1L, 2L));
        invitoRepository.save(new Invito(3L, 4L));
        assertEquals(2, invitoRepository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        invitoRepository.save(new Invito(1L, 2L));
        invitoRepository.deleteAll();
        assertEquals(0, invitoRepository.count());
    }

    @Test
    void testDeleteById() {
        Invito invito = invitoRepository.save(new Invito(1L, 2L));
        invitoRepository.deleteById(invito.getId());
        assertTrue(invitoRepository.findById(invito.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Invito invito = invitoRepository.save(new Invito(1L, 2L));
        assertTrue(invitoRepository.existsById(invito.getId()));
        assertFalse(invitoRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, invitoRepository.count());
        invitoRepository.save(new Invito(1L, 2L));
        assertEquals(1, invitoRepository.count());
    }

    @Test
    void testFindByTeamAndDestinatario() {
        invitoRepository.save(new Invito(1L, 2L));
        Optional<Invito> trovato = invitoRepository.findByTeamIdAndDestinatarioAndStato(1L, 2L, StatoInvito.IN_ATTESA);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByTeamAndDestinatarioNonInAttesa() {
        Invito invito = invitoRepository.save(new Invito(1L, 2L));
        invito.setStato(StatoInvito.RIFIUTATO);
        invitoRepository.save(invito);

        Optional<Invito> trovato = invitoRepository.findByTeamIdAndDestinatarioAndStato(1L, 2L, StatoInvito.IN_ATTESA);
        assertTrue(trovato.isEmpty());
    }

    @Test
    void testFindByDestinatarioId() {
        invitoRepository.save(new Invito(1L, 5L));
        invitoRepository.save(new Invito(2L, 5L));
        invitoRepository.save(new Invito(3L, 6L));

        List<Invito> inviti = invitoRepository.findByDestinatario(5L);
        assertEquals(2, inviti.size());
    }

    @Test
    void testFindByTeamId() {
        invitoRepository.save(new Invito(1L, 2L));
        invitoRepository.save(new Invito(1L, 3L));
        invitoRepository.save(new Invito(2L, 4L));

        List<Invito> inviti = invitoRepository.findByTeamId(1L);
        assertEquals(2, inviti.size());
    }

    @Test
    void testFindPendingByDestinatarioId() {
        invitoRepository.save(new Invito(1L, 5L));
        Invito i2 = invitoRepository.save(new Invito(2L, 5L));
        i2.setStato(StatoInvito.RIFIUTATO);
        invitoRepository.save(i2);

        List<Invito> pending = invitoRepository.findByDestinatarioAndStato(5L, StatoInvito.IN_ATTESA);
        assertEquals(1, pending.size());
    }

    @Test
    void testRifiutaAltriInviti() {
        Invito i1 = invitoRepository.save(new Invito(1L, 5L));
        Invito i2 = invitoRepository.save(new Invito(2L, 5L));
        Invito i3 = invitoRepository.save(new Invito(3L, 5L));

        // Manual logic: find all pending inviti for destinatario, then refuse all except i1
        List<Invito> pendenti = invitoRepository.findByDestinatarioAndStato(5L, StatoInvito.IN_ATTESA);
        for (Invito inv : pendenti) {
            if (!inv.getId().equals(i1.getId())) {
                inv.setStato(StatoInvito.RIFIUTATO);
                invitoRepository.save(inv);
            }
        }

        assertEquals(StatoInvito.IN_ATTESA, invitoRepository.findById(i1.getId()).orElseThrow().getStato());
        assertEquals(StatoInvito.RIFIUTATO, invitoRepository.findById(i2.getId()).orElseThrow().getStato());
        assertEquals(StatoInvito.RIFIUTATO, invitoRepository.findById(i3.getId()).orElseThrow().getStato());
    }
}
