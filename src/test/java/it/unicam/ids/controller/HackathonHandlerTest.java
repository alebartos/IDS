package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HackathonHandlerTest {

    private HackathonHandler hackathonHandler;
    private HackathonService hackathonService;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;
    private InvitoRepository invitoRepository;
    private TeamService teamService;
    private Utente organizzatore;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        invitoRepository = new InvitoRepository();

        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService);
        hackathonHandler = new HackathonHandler(hackathonService);

        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);
    }

    @Test
    void testCreaHackathonSuccess() {
        Result<Hackathon> response = hackathonHandler.creaHackathonRequest(
                "Hackathon 2025",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                "Test event",
                "Rules",
                LocalDate.of(2025, 2, 15),
                5,
                1000.0,
                organizzatore.getId());

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("Hackathon 2025", response.getData().getNome());
    }

    @Test
    void testCreaHackathonSenzaRuoloOrganizzatore() {
        Utente utenteNonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        utenteNonOrganizzatore = utenteRepository.save(utenteNonOrganizzatore);

        Result<Hackathon> response = hackathonHandler.creaHackathonRequest(
                "Hackathon No Org",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                "Test",
                "Rules",
                LocalDate.of(2025, 2, 15),
                5,
                5000.0,
                utenteNonOrganizzatore.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }
}
