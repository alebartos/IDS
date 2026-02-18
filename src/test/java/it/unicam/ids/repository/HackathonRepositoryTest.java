package it.unicam.ids.repository;

import it.unicam.ids.model.Hackathon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class HackathonRepositoryTest {

    @Autowired
    private HackathonRepository hackathonRepository;

    @Test
    void testAdd() {
        Hackathon h = new Hackathon();
        h.setNome("Hack Test");
        Hackathon saved = hackathonRepository.save(h);

        assertNotNull(saved.getId());
        assertEquals("Hack Test", saved.getNome());
    }

    @Test
    void testModifyRecord() {
        Hackathon h = new Hackathon();
        h.setNome("Originale");
        h = hackathonRepository.save(h);
        h.setNome("Modificato");
        hackathonRepository.save(h);

        assertEquals("Modificato", hackathonRepository.findById(h.getId()).orElseThrow().getNome());
    }

    @Test
    void testFindById() {
        Hackathon h = new Hackathon();
        h.setNome("Find Test");
        h = hackathonRepository.save(h);
        assertTrue(hackathonRepository.findById(h.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(hackathonRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        hackathonRepository.save(createHackathon("H1"));
        hackathonRepository.save(createHackathon("H2"));
        assertEquals(2, hackathonRepository.findAll().size());
    }

    @Test
    void testGetAllHackathon() {
        hackathonRepository.save(createHackathon("H1"));
        hackathonRepository.save(createHackathon("H2"));
        List<Hackathon> lista = hackathonRepository.findAll();
        assertEquals(2, lista.size());
    }

    @Test
    void testDeleteAll() {
        hackathonRepository.save(createHackathon("H1"));
        hackathonRepository.deleteAll();
        assertEquals(0, hackathonRepository.count());
    }

    @Test
    void testDeleteById() {
        Hackathon h = hackathonRepository.save(createHackathon("H1"));
        hackathonRepository.deleteById(h.getId());
        assertTrue(hackathonRepository.findById(h.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Hackathon h = hackathonRepository.save(createHackathon("H1"));
        assertTrue(hackathonRepository.existsById(h.getId()));
        assertFalse(hackathonRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, hackathonRepository.count());
        hackathonRepository.save(createHackathon("H1"));
        assertEquals(1, hackathonRepository.count());
    }

    @Test
    void testFindByName() {
        hackathonRepository.save(createHackathon("UniqueHack"));
        Optional<Hackathon> trovato = hackathonRepository.findByNome("UniqueHack");
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByNameNonEsistente() {
        assertTrue(hackathonRepository.findByNome("Inesistente").isEmpty());
    }

    @Test
    void testFindByIdOrganizzatore() {
        Hackathon h = createHackathon("Hack Org");
        h.setOrganizzatoreId(42L);
        hackathonRepository.save(h);

        Optional<Hackathon> trovato = hackathonRepository.findByOrganizzatoreId(42L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByIdOrganizzatoreNonEsistente() {
        assertTrue(hackathonRepository.findByOrganizzatoreId(999L).isEmpty());
    }

    private Hackathon createHackathon(String nome) {
        Hackathon h = new Hackathon();
        h.setNome(nome);
        h.setDataInizio(LocalDate.now());
        h.setDataFine(LocalDate.now().plusDays(5));
        return h;
    }
}
