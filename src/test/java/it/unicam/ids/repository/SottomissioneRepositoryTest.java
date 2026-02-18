package it.unicam.ids.repository;

import it.unicam.ids.model.Sottomissione;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class SottomissioneRepositoryTest {

    @Autowired
    private SottomissioneRepository sottomissioneRepository;

    @Test
    void testAdd() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        Sottomissione saved = sottomissioneRepository.save(s);
        assertNotNull(saved.getId());
    }

    @Test
    void testModifyRecord() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        s = sottomissioneRepository.save(s);
        s.setTeamId(2L);
        sottomissioneRepository.save(s);

        assertEquals(2L, sottomissioneRepository.findById(s.getId()).orElseThrow().getTeamId());
    }

    @Test
    void testFindById() {
        Sottomissione s = new Sottomissione();
        s = sottomissioneRepository.save(s);
        assertTrue(sottomissioneRepository.findById(s.getId()).isPresent());
    }

    @Test
    void testFindByIdNonEsistente() {
        assertTrue(sottomissioneRepository.findById(999L).isEmpty());
    }

    @Test
    void testFindAll() {
        sottomissioneRepository.save(new Sottomissione());
        sottomissioneRepository.save(new Sottomissione());
        assertEquals(2, sottomissioneRepository.findAll().size());
    }

    @Test
    void testDeleteAll() {
        sottomissioneRepository.save(new Sottomissione());
        sottomissioneRepository.deleteAll();
        assertEquals(0, sottomissioneRepository.count());
    }

    @Test
    void testDeleteById() {
        Sottomissione s = sottomissioneRepository.save(new Sottomissione());
        sottomissioneRepository.deleteById(s.getId());
        assertTrue(sottomissioneRepository.findById(s.getId()).isEmpty());
    }

    @Test
    void testExistsById() {
        Sottomissione s = sottomissioneRepository.save(new Sottomissione());
        assertTrue(sottomissioneRepository.existsById(s.getId()));
        assertFalse(sottomissioneRepository.existsById(999L));
    }

    @Test
    void testCount() {
        assertEquals(0, sottomissioneRepository.count());
        sottomissioneRepository.save(new Sottomissione());
        assertEquals(1, sottomissioneRepository.count());
    }

    @Test
    void testFindByTeamAndHackathon() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(2L);
        sottomissioneRepository.save(s);

        Optional<Sottomissione> trovato = sottomissioneRepository.findByTeamIdAndHackathonId(1L, 2L);
        assertTrue(trovato.isPresent());
    }

    @Test
    void testFindByTeamAndHackathonNonEsistente() {
        assertTrue(sottomissioneRepository.findByTeamIdAndHackathonId(99L, 99L).isEmpty());
    }

    @Test
    void testFindByTeamId() {
        Sottomissione s1 = new Sottomissione();
        s1.setTeamId(1L);
        sottomissioneRepository.save(s1);
        Sottomissione s2 = new Sottomissione();
        s2.setTeamId(1L);
        sottomissioneRepository.save(s2);
        Sottomissione s3 = new Sottomissione();
        s3.setTeamId(2L);
        sottomissioneRepository.save(s3);

        List<Sottomissione> lista = sottomissioneRepository.findByTeamId(1L);
        assertEquals(2, lista.size());
    }

    @Test
    void testFindByHackathonId() {
        Sottomissione s1 = new Sottomissione();
        s1.setHackathonId(5L);
        sottomissioneRepository.save(s1);
        Sottomissione s2 = new Sottomissione();
        s2.setHackathonId(5L);
        sottomissioneRepository.save(s2);

        List<Sottomissione> lista = sottomissioneRepository.findByHackathonId(5L);
        assertEquals(2, lista.size());
    }
}
