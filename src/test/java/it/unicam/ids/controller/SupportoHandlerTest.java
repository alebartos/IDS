package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.SupportoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.CalendarService;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.NotificationService;
import it.unicam.ids.service.ObserverSupporto;
import it.unicam.ids.service.SupportoService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupportoHandlerTest {

    private SupportoHandler supportoHandler;
    private SupportoRepository supportoRepository;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;

    private Utente membro;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        supportoRepository = new SupportoRepository();
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        InvitoRepository invitoRepository = new InvitoRepository();

        CalendarService calendarService = new CalendarService();
        NotificationService notificationService = new NotificationService();
        ObserverSupporto observerSupporto = new ObserverSupporto(notificationService);

        TeamService teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        HackathonService hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        SupportoService supportoService = new SupportoService(supportoRepository, hackathonRepository,
                utenteRepository, teamRepository, calendarService, observerSupporto);
        supportoHandler = new SupportoHandler(supportoService);

        // Setup dati
        Utente organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        Utente leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.add(leader);
        team = teamService.createTeam("Team Test", leader.getId());

        membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathonRepository.modifyRecord(hackathon);
    }

    @Test
    void testInviaRichiestaSupportoSuccess() {
        Result<String> result = supportoHandler.inviaRichiestaSupporto(
                "Problema tecnico", membro.getId(), hackathon.getId());

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testInviaRichiestaSupportoNonPartecipante() {
        Utente nonPartecipante = new Utente("Test", "User", "test@example.com", "pass");
        nonPartecipante = utenteRepository.add(nonPartecipante);

        Result<String> result = supportoHandler.inviaRichiestaSupporto(
                "Problema", nonPartecipante.getId(), hackathon.getId());

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testGetRichieste() {
        supportoHandler.inviaRichiestaSupporto("Problema", membro.getId(), hackathon.getId());

        Result<List<RichiestaSupporto>> result = supportoHandler.getRichieste(hackathon.getId());

        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testPrenotaCallSuccess() {
        supportoHandler.inviaRichiestaSupporto("Problema", membro.getId(), hackathon.getId());
        RichiestaSupporto richiesta = supportoRepository.getAllRichieste(hackathon.getId()).get(0);

        Result<String> result = supportoHandler.prenotaCall(
                richiesta.getId(), LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testPrenotaCallDateNulle() {
        Result<String> result = supportoHandler.prenotaCall(1L, null, LocalTime.of(10, 0), LocalTime.of(11, 0));

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
