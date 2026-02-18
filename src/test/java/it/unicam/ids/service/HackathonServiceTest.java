package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HackathonServiceTest {

    private HackathonService hackathonService;
    private TeamService teamService;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;
    private InvitoRepository invitoRepository;

    private Utente organizzatore;
    private Utente leader;
    private Team team;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        invitoRepository = new InvitoRepository();

        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);

        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.add(leader);

        team = teamService.createTeam("Team Test", leader.getId());
    }

    @Test
    void testCreaHackathonSuccess() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon 2025", "Test event",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                5, 1000.0, organizzatore.getId());

        assertNotNull(hackathon);
        assertNotNull(hackathon.getId());
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals(organizzatore.getId(), hackathon.getOrganizzatoreId());
    }

    @Test
    void testCreaHackathonDuplicateNome() {
        hackathonService.createHackathon(
                "Duplicate Hackathon", "First",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                5, 5000.0, organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.createHackathon(
                        "Duplicate Hackathon", "Second",
                        LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 3),
                        LocalDate.of(2025, 3, 15),
                        5, 5000.0, organizzatore.getId()));
    }

    @Test
    void testCreaHackathonSenzaRuoloOrganizzatore() {
        Utente utenteNonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        utenteNonOrganizzatore = utenteRepository.add(utenteNonOrganizzatore);
        final Long utenteId = utenteNonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.createHackathon(
                        "Hackathon No Org", "Test",
                        LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 3),
                        LocalDate.of(2025, 2, 15),
                        5, 5000.0, utenteId));
    }

    @Test
    void testAssegnaGiudiceSuccess() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Giudice Test", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice.getRuoli().add(Ruolo.MEMBRO_STAFF);
        giudice = utenteRepository.add(giudice);

        hackathonService.assegnaGiudice(hackathon.getId(), "paolo@example.com", organizzatore.getId());

        Hackathon aggiornato = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertNotNull(aggiornato.getGiudiceId());
        assertEquals(giudice.getId(), aggiornato.getGiudiceId());
        assertTrue(giudice.getRuoli().contains(Ruolo.GIUDICE));
    }

    @Test
    void testAssegnaGiudiceNonOrganizzatore() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Giudice Non Org", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice.getRuoli().add(Ruolo.MEMBRO_STAFF);
        giudice = utenteRepository.add(giudice);

        Utente nonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        nonOrganizzatore = utenteRepository.add(nonOrganizzatore);
        final Long nonOrganizzatoreId = nonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), "paolo@example.com", nonOrganizzatoreId));
    }

    @Test
    void testAssegnaGiudiceGiaAssegnato() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Doppio Giudice", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        Utente giudice1 = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice1.getRuoli().add(Ruolo.MEMBRO_STAFF);
        giudice1 = utenteRepository.add(giudice1);
        Utente giudice2 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        giudice2.getRuoli().add(Ruolo.MEMBRO_STAFF);
        giudice2 = utenteRepository.add(giudice2);

        hackathonService.assegnaGiudice(hackathon.getId(), "paolo@example.com", organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), "anna@example.com", organizzatore.getId()));
    }

    @Test
    void testAssegnaGiudiceEmailNull() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Giudice Null", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), null, organizzatore.getId()));
    }

    @Test
    void testAssegnaMentoreSuccess() {
        hackathonService.createHackathon(
                "Hackathon Mentore Test", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        Utente mentore = new Utente("Marco", "Neri", "marco@example.com", "password");
        mentore = utenteRepository.add(mentore);

        hackathonService.assegnaMentore("marco@example.com", organizzatore.getId());

        assertTrue(mentore.getRuoli().contains(Ruolo.MENTORE));
    }

    @Test
    void testCheckRuolo() {
        assertTrue(hackathonService.checkRuolo(Ruolo.ORGANIZZATORE));
        assertTrue(hackathonService.checkRuolo(Ruolo.LEADER));
        assertFalse(hackathonService.checkRuolo(null));
    }

    @Test
    void testCheckId() {
        assertTrue(hackathonService.checkId(organizzatore.getId()));
        assertFalse(hackathonService.checkId(null));
        assertFalse(hackathonService.checkId(9999L));
    }

    @Test
    void testFindByTeamId() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Find Test", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        hackathon.getTeamIds().add(team.getId());
        hackathonRepository.modifyRecord(hackathon);

        assertTrue(hackathonService.findByTeamId(team.getId()));
        assertFalse(hackathonService.findByTeamId(9999L));
    }

    @Test
    void testModifcaStatoInCorso() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Stato Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 5000.0, organizzatore.getId());

        hackathonService.modifcaStato(hackathon.getId(), StatoHackathon.IN_CORSO);

        Hackathon aggiornato = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertEquals(StatoHackathon.IN_CORSO, aggiornato.getStato());
    }

    @Test
    void testModifcaStatoTransizioneNonValida() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Stato Invalid", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                5, 5000.0, organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.modifcaStato(hackathon.getId(), StatoHackathon.CONCLUSO));
    }

    @Test
    void testProclamaVincitore() {
        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Vincitore Test", "Description",
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(6),
                5, 5000.0, organizzatore.getId());

        hackathon.getTeamIds().add(team.getId());
        hackathon.setStato(StatoHackathon.IN_VALUTAZIONE);
        hackathonRepository.modifyRecord(hackathon);

        hackathonService.proclamaVincitore(hackathon.getId(), team.getId());

        Hackathon aggiornato = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertEquals(team.getId(), aggiornato.getTeamVincitoreId());
        assertEquals(StatoHackathon.CONCLUSO, aggiornato.getStato());
    }

    @Test
    void testCreaListaHackathon() {
        hackathonService.createHackathon(
                "Hack Lista 1", "Description",
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                5, 1000.0, organizzatore.getId());
        hackathonService.createHackathon(
                "Hack Lista 2", "Description",
                LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 3),
                LocalDate.of(2025, 3, 15),
                5, 2000.0, organizzatore.getId());

        List<Hackathon> lista = hackathonService.creaListaHackathon();
        assertEquals(2, lista.size());
    }

    @Test
    void testCreaListaHackathonVuota() {
        List<Hackathon> lista = hackathonService.creaListaHackathon();
        assertTrue(lista.isEmpty());
    }

    @Test
    void testGetHackathons() {
        hackathonService.createHackathon(
                "Hack Get 1", "Description",
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 3),
                LocalDate.of(2025, 4, 15),
                5, 1000.0, organizzatore.getId());

        List<Hackathon> hackathons = hackathonService.getHackathons();
        assertEquals(1, hackathons.size());
        assertEquals("Hack Get 1", hackathons.get(0).getNome());
    }
}
