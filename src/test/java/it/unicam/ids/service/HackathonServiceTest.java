package it.unicam.ids.service;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Organizzatore;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.OrganizzatoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HackathonServiceTest {

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private OrganizzatoreRepository organizzatoreRepository;

    private Organizzatore organizzatore;

    @BeforeEach
    void setUp() {
        hackathonRepository.deleteAll();
        organizzatoreRepository.deleteAll();

        organizzatore = new Organizzatore("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore = organizzatoreRepository.save(organizzatore);
    }

    @Test
    void testCreaHackathonSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon 2025",
                "Test event",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "1000 Euro",
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        assertNotNull(hackathon);
        assertNotNull(hackathon.getId());
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals("Milano", hackathon.getLuogo());
    }

    @Test
    void testCreaHackathonDuplicateNome() {
        HackathonRequest request1 = new HackathonRequest(
                "Duplicate Hackathon",
                "First",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        hackathonService.creaHackathon(organizzatore, request1);

        HackathonRequest request2 = new HackathonRequest(
                "Duplicate Hackathon",
                "Second",
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 4, 3),
                LocalDate.of(2025, 3, 15),
                "Roma",
                "Rules",
                "Prize",
                5
        );

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.creaHackathon(organizzatore, request2));
    }

    @Test
    void testValidaDate() {
        LocalDate inizio = LocalDate.of(2025, 3, 1);
        LocalDate fine = LocalDate.of(2025, 3, 3);
        LocalDate scadenza = LocalDate.of(2025, 2, 15);

        assertTrue(hackathonService.validaDate(inizio, fine, scadenza));

        assertFalse(hackathonService.validaDate(inizio, fine, LocalDate.of(2025, 3, 2)));

        assertFalse(hackathonService.validaDate(inizio, inizio, scadenza));

        assertFalse(hackathonService.validaDate(fine, inizio, scadenza));
    }

    @Test
    void testEsisteHackathonConNome() {
        HackathonRequest request = new HackathonRequest(
                "Test Hackathon",
                "Description",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        hackathonService.creaHackathon(organizzatore, request);

        assertTrue(hackathonService.esisteHackathonConNome("Test Hackathon"));
        assertFalse(hackathonService.esisteHackathonConNome("NonEsistente"));
    }

    @Test
    void testGetDettagliHackathon() {
        HackathonRequest request = new HackathonRequest(
                "Details Test",
                "Description",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        Hackathon created = hackathonService.creaHackathon(organizzatore, request);
        Hackathon retrieved = hackathonService.getDettagliHackathon(created.getId());

        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Details Test", retrieved.getNome());
    }

    @Test
    void testGetMaxMembriTeam() {
        HackathonRequest request = new HackathonRequest(
                "MaxMembri Test",
                "Description",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "Prize",
                7
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);
        int maxMembri = hackathonService.getMaxMembriTeam(hackathon.getId());

        assertEquals(7, maxMembri);
    }

    @Test
    void testCheckValiditaHackathon() {
        HackathonRequest requestFuturo = new HackathonRequest(
                "Futuro Hackathon",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        Hackathon hackathonFuturo = hackathonService.creaHackathon(organizzatore, requestFuturo);
        assertTrue(hackathonService.checkValiditaHackathon(hackathonFuturo.getId()));

        HackathonRequest requestPassato = new HackathonRequest(
                "Passato Hackathon",
                "Description",
                LocalDate.now().minusMonths(1),
                LocalDate.now().minusMonths(1).plusDays(3),
                LocalDate.now().minusMonths(2),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        Hackathon hackathonPassato = hackathonService.creaHackathon(organizzatore, requestPassato);
        assertFalse(hackathonService.checkValiditaHackathon(hackathonPassato.getId()));
    }
}
