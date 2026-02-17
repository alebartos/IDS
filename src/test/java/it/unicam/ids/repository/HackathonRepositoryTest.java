package it.unicam.ids.repository;

import it.unicam.ids.model.Hackathon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HackathonRepositoryTest {

    private HackathonRepository hackathonRepository;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
    }

    @Test
    void testAdd() {
        Hackathon h = new Hackathon();
        h.setNome("Hack Test");
        Hackathon saved = hackathonRepository.add(h);

        assertNotNull(saved.getId());
        assertEquals("Hack Test", saved.getNome());
    }

    @Test
    void testAddConIdEsistente() {
        Hackathon h = new Hackathon();
        h.setId(100L);
        h.setNome("Hack ID");
        Hackathon saved = hackathonRepository.add(h);
        assertEquals(100L, saved.getId());
    }

    @Test
    void testModifyRecord() {
        Hackathon h = new Hackathon();
        h.setNome("Originale");
        h = hackathonRepository.add(h);
        h.setNome("Modificato");
        hackathonRepository.modifyRecord(h);

        assertEquals("Modificato", hackathonRepository.findById(h.getId()).orElseThrow().getNome());
    }

    @Test
    void testFindById() {
        Hackathon h = new Hackathon();
        h.setNome("Find Test");
        h = hackathonRepository.add(h);
        assertTrue(hackathonRepository.findById(h.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(hackathonRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        hackathonRepository.add(createHackathon("H1"));
        hackathonRepository.add(createHackathon("H2"));
        assertEquals(2, hackathonRepository.findAll().size());
    }

    @Test
    void testGetAllHackathon() {
        hackathonRepository.add(createHackathon("H1"));
        hackathonRepository.add(createHackathon("H2"));
        List<Hackathon> lista = hackathonRepository.getAllHackathon();
        assertEquals(2, lista.size());
    }

    @Test
    void testDeleteAll() {
        hackathonRepository.add(createHackathon("H1"));
        hackathonRepository.deleteAll();
        assertEquals(0, hackathonRepository.count());
    }

    @Test
    void testDeleteById() {
        Hackathon h = hackathonRepository.add(createHackathon("H1"));
        hackathonRepository.deleteById(h.getId());
        assertTrue(hackathonRepository.findById(h.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Hackathon h = hackathonRepository.add(createHackathon("H1"));
        assertTrue(hackathonRepository.existsById(h.getId()));
        assertFalse(hackathonRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, hackathonRepository.count());
        hackathonRepository.add(createHackathon("H1"));
        assertEquals(1, hackathonRepository.count());
    }

    @Test
    void testFindByName() {
        hackathonRepository.add(createHackathon("UniqueHack"));
        Optional<Hackathon> trovato = hackathonRepository.findByName("UniqueHack");
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByNameNonEsistente() {
        assertTrue(hackathonRepository.findByName("Inesistente").isEmpty());
    }

    @Test
    void testFindByIdOrganizzatore() {
        Hackathon h = createHackathon("Hack Org");
        h.setOrganizzatoreId(42L);
        hackathonRepository.add(h);

        Optional<Hackathon> trovato = hackathonRepository.findByIdOrganizzatore(42L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByIdOrganizzatoreNonEsistente() {
        assertTrue(hackathonRepository.findByIdOrganizzatore(999L).isEmpty());
    }

    private Hackathon createHackathon(String nome) {
        Hackathon h = new Hackathon();
        h.setNome(nome);
        h.setDataInizio(LocalDate.now());
        h.setDataFine(LocalDate.now().plusDays(5));
        return h;
    }
}
