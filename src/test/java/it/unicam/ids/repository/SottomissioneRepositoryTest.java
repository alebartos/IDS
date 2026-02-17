package it.unicam.ids.repository;

import it.unicam.ids.model.Sottomissione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneRepositoryTest {

    private SottomissioneRepository sottomissioneRepository;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
    }

    @Test
    void testAdd() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        Sottomissione saved = sottomissioneRepository.add(s);
        assertNotNull(saved.getId());
    }

    @Test
    void testModifyRecord() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        s = sottomissioneRepository.add(s);
        s.setTeamId(2L);
        sottomissioneRepository.modifyRecord(s);

        assertEquals(2L, sottomissioneRepository.findById(s.getId()).orElseThrow().getTeamId());
    }

    @Test
    void testFindById() {
        Sottomissione s = new Sottomissione();
        s = sottomissioneRepository.add(s);
        assertTrue(sottomissioneRepository.findById(s.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(sottomissioneRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        sottomissioneRepository.add(new Sottomissione());
        sottomissioneRepository.add(new Sottomissione());
        assertEquals(2, sottomissioneRepository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        sottomissioneRepository.add(new Sottomissione());
        sottomissioneRepository.deleteAll();
        assertEquals(0, sottomissioneRepository.count());
    }

    @Test
    void testDeleteById() {
        Sottomissione s = sottomissioneRepository.add(new Sottomissione());
        sottomissioneRepository.deleteById(s.getId());
        assertTrue(sottomissioneRepository.findById(s.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Sottomissione s = sottomissioneRepository.add(new Sottomissione());
        assertTrue(sottomissioneRepository.existsById(s.getId()));
        assertFalse(sottomissioneRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, sottomissioneRepository.count());
        sottomissioneRepository.add(new Sottomissione());
        assertEquals(1, sottomissioneRepository.count());
    }

    @Test
    void testFindByTeamAndHackathon() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(2L);
        sottomissioneRepository.add(s);

        Optional<Sottomissione> trovato = sottomissioneRepository.findByTeamAndHackathon(1L, 2L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByTeamAndHackathonNonEsistente() {
        assertTrue(sottomissioneRepository.findByTeamAndHackathon(99L, 99L).isEmpty());
    }

    @Test
    void testFindByTeamId() {
        Sottomissione s1 = new Sottomissione();
        s1.setTeamId(1L);
        sottomissioneRepository.add(s1);
        Sottomissione s2 = new Sottomissione();
        s2.setTeamId(1L);
        sottomissioneRepository.add(s2);
        Sottomissione s3 = new Sottomissione();
        s3.setTeamId(2L);
        sottomissioneRepository.add(s3);

        List<Sottomissione> lista = sottomissioneRepository.findByTeamId(1L);
        assertEquals(2, lista.size());
    }

    @Test
    void testFindByHackathonId() {
        Sottomissione s1 = new Sottomissione();
        s1.setHackathonId(5L);
        sottomissioneRepository.add(s1);
        Sottomissione s2 = new Sottomissione();
        s2.setHackathonId(5L);
        sottomissioneRepository.add(s2);

        List<Sottomissione> lista = sottomissioneRepository.findByHackathonId(5L);
        assertEquals(2, lista.size());
    }
}
