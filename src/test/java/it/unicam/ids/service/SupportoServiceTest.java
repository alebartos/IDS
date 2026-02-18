package it.unicam.ids.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupportoServiceTest {

    private SupportoService supportoService;
    private SupportoRepository supportoRepository;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;
    private TeamService teamService;
    private HackathonService hackathonService;

    private Utente organizzatore;
    private Utente leader;
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

        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        supportoService = new SupportoService(supportoRepository, hackathonRepository,
                utenteRepository, teamRepository, calendarService, observerSupporto);

        // Crea organizzatore
        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        // Crea leader e team
        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.add(leader);
        team = teamService.createTeam("Team Test", leader.getId());

        // Crea membro del team
        membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        // Crea hackathon IN_CORSO
        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathonRepository.modifyRecord(hackathon);
    }

    @Test
    void testCheckRuoloPartecipante() {
        Utente partecipante = new Utente("Test", "User", "test@example.com", "pass");
        partecipante.getRuoli().add(Ruolo.PARTECIPANTE);
        partecipante = utenteRepository.add(partecipante);

        assertTrue(supportoService.checkRuolo(partecipante.getId()));
    }

    @Test
    void testCheckRuoloMembroTeam() {
        assertTrue(supportoService.checkRuolo(membro.getId()));
    }

    @Test
    void testCheckRuoloNonPartecipante() {
        assertFalse(supportoService.checkRuolo(organizzatore.getId()));
    }

    @Test
    void testCheckRuoloUtenteNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> supportoService.checkRuolo(9999L));
    }

    @Test
    void testCheckStatoInCorso() {
        assertTrue(supportoService.checkStato(hackathon.getId()));
    }

    @Test
    void testCheckStatoNonInCorso() {
        Hackathon altro = hackathonService.createHackathon(
                "Altro Hackathon", "Desc",
                LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2),
                LocalDate.now().plusDays(15),
                5, 1000.0, organizzatore.getId());

        assertFalse(supportoService.checkStato(altro.getId()));
    }

    @Test
    void testElaboraRichiestaSupportoSuccess() {
        assertDoesNotThrow(() ->
                supportoService.elaboraRichiestaSupporto("Problema tecnico", membro.getId(), hackathon.getId()));

        List<RichiestaSupporto> richieste = supportoService.creaListaRichieste(hackathon.getId());
        assertEquals(1, richieste.size());
        assertEquals("Problema tecnico", richieste.get(0).getDescrizione());
    }

    @Test
    void testElaboraRichiestaSupportoNonPartecipante() {
        assertThrows(IllegalArgumentException.class,
                () -> supportoService.elaboraRichiestaSupporto("Problema", organizzatore.getId(), hackathon.getId()));
    }

    @Test
    void testElaboraRichiestaSupportoHackathonNonAttivo() {
        Hackathon nonAttivo = hackathonService.createHackathon(
                "Non Attivo", "Desc",
                LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(2),
                LocalDate.now().plusDays(15),
                5, 1000.0, organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> supportoService.elaboraRichiestaSupporto("Problema", membro.getId(), nonAttivo.getId()));
    }

    @Test
    void testCreaListaRichieste() {
        supportoService.elaboraRichiestaSupporto("Richiesta 1", membro.getId(), hackathon.getId());

        List<RichiestaSupporto> lista = supportoService.creaListaRichieste(hackathon.getId());
        assertEquals(1, lista.size());
    }

    @Test
    void testCreaListaRichiesteVuota() {
        List<RichiestaSupporto> lista = supportoService.creaListaRichieste(hackathon.getId());
        assertTrue(lista.isEmpty());
    }

    @Test
    void testPrenotaCallSuccess() {
        supportoService.elaboraRichiestaSupporto("Problema", membro.getId(), hackathon.getId());
        RichiestaSupporto richiesta = supportoService.creaListaRichieste(hackathon.getId()).get(0);

        LocalDate data = LocalDate.now().plusDays(1);
        LocalTime oraInizio = LocalTime.of(10, 0);
        LocalTime oraFine = LocalTime.of(11, 0);

        assertDoesNotThrow(() -> supportoService.prenotaCall(richiesta.getId(), data, oraInizio, oraFine));

        RichiestaSupporto aggiornata = supportoRepository.findById(richiesta.getId()).orElseThrow();
        assertTrue(aggiornata.isRisolta());
    }

    @Test
    void testPrenotaCallDateNulle() {
        assertThrows(IllegalArgumentException.class,
                () -> supportoService.prenotaCall(1L, null, LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }

    @Test
    void testPrenotaCallOrariInvertiti() {
        assertThrows(IllegalArgumentException.class,
                () -> supportoService.prenotaCall(1L, LocalDate.now(), LocalTime.of(15, 0), LocalTime.of(10, 0)));
    }

    @Test
    void testCheckDateValide() {
        assertDoesNotThrow(() ->
                supportoService.checkDate(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }

    @Test
    void testCheckDateStessoOrario() {
        assertDoesNotThrow(() ->
                supportoService.checkDate(LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(10, 0)));
    }

    @Test
    void testCreaListaMail() {
        List<String> mails = supportoService.creaListaMail(team);

        assertTrue(mails.contains("anna@example.com"));
        assertTrue(mails.contains("mario@example.com"));
    }
}
