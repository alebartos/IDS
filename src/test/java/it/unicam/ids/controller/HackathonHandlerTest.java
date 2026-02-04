package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.OrganizzatoreRepository;
import it.unicam.ids.service.HackathonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HackathonHandlerTest {

    private HackathonHandler hackathonHandler;
    private HackathonService hackathonService;
    private HackathonRepository hackathonRepository;
    private OrganizzatoreRepository organizzatoreRepository;
    private Organizzatore organizzatore;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        organizzatoreRepository = new OrganizzatoreRepository();
        hackathonService = new HackathonService(hackathonRepository);
        hackathonHandler = new HackathonHandler(hackathonService, organizzatoreRepository);

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

        Result<Hackathon> response = hackathonHandler.creaHackathon(request, organizzatore.getId());

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("Hackathon 2025", response.getData().getNome());
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

        Result<Hackathon> response = hackathonHandler.getDettagliHackathon(hackathon.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertEquals("Details Hackathon", response.getData().getNome());
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

        Result<Integer> response = hackathonHandler.getMaxMembriTeam(hackathon.getId());

        assertEquals(200, response.getStatusCode());
        assertEquals(7, response.getData());
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

        Result<Boolean> response = hackathonHandler.checkValidita(hackathon.getId());

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getData());
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

        Result<Boolean> existsResponse = hackathonHandler.esisteHackathon("Exists Hackathon");
        Result<Boolean> notExistsResponse = hackathonHandler.esisteHackathon("NonEsistente");

        assertTrue(existsResponse.getData());
        assertFalse(notExistsResponse.getData());
    }
}
