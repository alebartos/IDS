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

import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;

import java.time.LocalDate;
import java.util.List;

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
        organizzatore = utenteRepository.add(organizzatore);
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
        utenteNonOrganizzatore = utenteRepository.add(utenteNonOrganizzatore);

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

    @Test
    void testAssegnaGiudiceSuccess() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Giudice", "Desc",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice.getRuoli().add(Ruolo.MEMBRO_STAFF);
        utenteRepository.add(giudice);

        Result<String> result = hackathonHandler.assegnaGiudice(
                hackathon.getId(), "paolo@example.com", organizzatore.getId());
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testAssegnaGiudiceNonOrganizzatore() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Giudice NO", "Desc",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());

        Utente nonOrg = new Utente("Test", "User", "test@example.com", "pass");
        nonOrg = utenteRepository.add(nonOrg);

        Result<String> result = hackathonHandler.assegnaGiudice(
                hackathon.getId(), "test@example.com", nonOrg.getId());
        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testAssegnaMentoreSuccess() {
        hackathonService.createHackathon(
                "Hack Mentore", "Desc",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());

        Utente mentore = new Utente("Marco", "Neri", "marco@example.com", "password");
        utenteRepository.add(mentore);

        Result<String> result = hackathonHandler.assegnaMentore(
                "marco@example.com", organizzatore.getId());
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testAssegnaMentoreNonOrganizzatore() {
        Utente nonOrg = new Utente("Test", "User", "test@example.com", "pass");
        nonOrg = utenteRepository.add(nonOrg);

        Result<String> result = hackathonHandler.assegnaMentore(
                "test@example.com", nonOrg.getId());
        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testRefreshDettagli() {
        Result<String> result = hackathonHandler.refreshDettagli();
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testCambiaStatoSuccess() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Stato", "Desc",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                5, 5000.0, organizzatore.getId());

        Result<String> result = hackathonHandler.cambiaStato(
                hackathon.getId(), StatoHackathon.IN_CORSO);
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testCambiaStatoTransizioneNonValida() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Stato NV", "Desc",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());

        Result<String> result = hackathonHandler.cambiaStato(
                hackathon.getId(), StatoHackathon.CONCLUSO);
        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testProclamaVincitoreSuccess() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Vincitore", "Desc",
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1),
                5, 5000.0, organizzatore.getId());

        Utente leaderTeam = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leaderTeam = utenteRepository.add(leaderTeam);
        Team team = teamService.createTeam("Team Vincitore", leaderTeam.getId());

        hackathon.getTeamIds().add(team.getId());
        hackathon.setStato(StatoHackathon.IN_VALUTAZIONE);
        hackathonRepository.modifyRecord(hackathon);

        Result<String> result = hackathonHandler.proclamaVincitore(
                hackathon.getId(), team.getId());
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testProclamaVincitoreHackathonNonInValutazione() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hack Vincitore NV", "Desc",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());

        Result<String> result = hackathonHandler.proclamaVincitore(
                hackathon.getId(), 1L);
        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testGetHackathons() {
        hackathonService.createHackathon(
                "Hack Get 1", "Desc",
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 3),
                5, 1000.0, organizzatore.getId());
        hackathonService.createHackathon(
                "Hack Get 2", "Desc",
                LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 3),
                5, 2000.0, organizzatore.getId());

        Result<List<Hackathon>> result = hackathonHandler.getHackathons();
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetHackathonsVuota() {
        Result<List<Hackathon>> result = hackathonHandler.getHackathons();
        assertTrue(result.isSuccess());
        assertTrue(result.getData().isEmpty());
    }
}
