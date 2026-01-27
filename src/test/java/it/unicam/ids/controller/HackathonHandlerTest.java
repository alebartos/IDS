package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Organizzatore;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.OrganizzatoreRepository;
import it.unicam.ids.service.HackathonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HackathonHandlerTest {

    @Autowired
    private HackathonHandler hackathonHandler;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private OrganizzatoreRepository organizzatoreRepository;

    @Autowired
    private HackathonService hackathonService;

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

        ResponseEntity<?> response = hackathonHandler.creaHackathon(request, organizzatore.getId());

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Hackathon);
        Hackathon hackathon = (Hackathon) response.getBody();
        assertEquals("Hackathon 2025", hackathon.getNome());
    }

    @Test
    void testGetDettagliHackathon() {
        HackathonRequest request = new HackathonRequest(
                "Details Hackathon",
                "Description",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        ResponseEntity<?> response = hackathonHandler.getDettagliHackathon(hackathon.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof Hackathon);
        Hackathon retrieved = (Hackathon) response.getBody();
        assertEquals("Details Hackathon", retrieved.getNome());
    }

    @Test
    void testGetMaxMembriTeam() {
        HackathonRequest request = new HackathonRequest(
                "MaxMembri Hackathon",
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

        ResponseEntity<?> response = hackathonHandler.getMaxMembriTeam(hackathon.getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(7, response.getBody());
    }

    @Test
    void testCheckValidita() {
        HackathonRequest request = new HackathonRequest(
                "Valido Hackathon",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                "Prize",
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        ResponseEntity<Boolean> response = hackathonHandler.checkValidita(hackathon.getId());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody());
    }

    @Test
    void testEsisteHackathon() {
        HackathonRequest request = new HackathonRequest(
                "Exists Hackathon",
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

        ResponseEntity<Boolean> existsResponse = hackathonHandler.esisteHackathon("Exists Hackathon");
        ResponseEntity<Boolean> notExistsResponse = hackathonHandler.esisteHackathon("NonEsistente");

        assertTrue(existsResponse.getBody());
        assertFalse(notExistsResponse.getBody());
    }
}
