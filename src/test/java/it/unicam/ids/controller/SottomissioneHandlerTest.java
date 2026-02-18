package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.SottomissioneService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneHandlerTest {

    private SottomissioneHandler sottomissioneHandler;
    private SottomissioneRepository sottomissioneRepository;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;

    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        InvitoRepository invitoRepository = new InvitoRepository();

        TeamService teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        HackathonService hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        SottomissioneService sottomissioneService = new SottomissioneService(sottomissioneRepository, hackathonRepository, utenteRepository);
        sottomissioneHandler = new SottomissioneHandler(sottomissioneService);

        Utente organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        Utente leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.add(leader);
        team = teamService.createTeam("Team Alpha", leader.getId());

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathon.getTeamIds().add(team.getId());
        hackathonRepository.modifyRecord(hackathon);
    }

    @Test
    void testCaricaBozzaSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(team.getId(), hackathon.getId(), dati, false);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getData());
        assertEquals(StatoSottomissione.BOZZA, result.getData().getStato());
    }

    @Test
    void testCaricaBozzaDefinitiva() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(team.getId(), hackathon.getId(), dati, true);

        assertTrue(result.isSuccess());
        assertEquals(StatoSottomissione.CONSEGNATA, result.getData().getStato());
    }

    @Test
    void testCaricaBozzaGiaConsegnata() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");
        sottomissioneHandler.caricaBozza(team.getId(), hackathon.getId(), dati, true);

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(team.getId(), hackathon.getId(), dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(team.getId(), hackathon.getId(), dati, false);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerDefinitiva() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(team.getId(), hackathon.getId(), dati, true);

        assertTrue(result.isSuccess());
        assertEquals(StatoSottomissione.CONSEGNATA, result.getData().getStato());
    }

    @Test
    void testSottomissioneHandlerLinkNonValido() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "link-non-valido");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(team.getId(), hackathon.getId(), dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerLinkVuoto() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(team.getId(), hackathon.getId(), dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
