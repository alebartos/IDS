package it.unicam.ids.service;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HackathonServiceTest {

    private HackathonService hackathonService;
    private TeamService teamService;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;

    private Utente organizzatore;
    private Utente leader;
    private Team team;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();

        hackathonService = new HackathonService(hackathonRepository, utenteRepository);
        teamService = new TeamService(teamRepository, utenteRepository);

        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        TeamRequest teamRequest = new TeamRequest("Team Test", "Descrizione test", leader.getId());
        team = teamService.creaTeam(teamRequest);
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
                1000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        assertNotNull(hackathon);
        assertNotNull(hackathon.getId());
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals("Milano", hackathon.getLuogo());
        assertEquals(organizzatore.getId(), hackathon.getOrganizzatoreId());
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
                5000.0,
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
                5000.0,
                5
        );

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.creaHackathon(organizzatore, request2));
    }

    @Test
    void testCreaHackathonSenzaRuoloOrganizzatore() {
        Utente utenteNonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        utenteNonOrganizzatore = utenteRepository.save(utenteNonOrganizzatore);
        final Utente utente = utenteNonOrganizzatore;

        HackathonRequest request = new HackathonRequest(
                "Hackathon No Org",
                "Test",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 3),
                LocalDate.of(2025, 2, 15),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.creaHackathon(utente, request));
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
                5000.0,
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
                5000.0,
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
                5000.0,
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
                5000.0,
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
                5000.0,
                5
        );

        Hackathon hackathonPassato = hackathonService.creaHackathon(organizzatore, requestPassato);
        assertFalse(hackathonService.checkValiditaHackathon(hackathonPassato.getId()));
    }

    @Test
    void testIscriviTeamSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Iscrizione",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        // Il leader del team iscrive il team
        assertDoesNotThrow(() -> hackathonService.iscriviTeam(hackathon, team, leader.getId()));
    }

    @Test
    void testIscriviTeamNonLeader() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Non Leader",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();

        // Un non-leader tenta di iscrivere il team
        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.iscriviTeam(hackathon, team, nonLeaderId));
    }

    @Test
    void testIscriviTeamHackathonChiuso() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Chiuso",
                "Description",
                LocalDate.now().minusMonths(1),
                LocalDate.now().minusMonths(1).plusDays(3),
                LocalDate.now().minusMonths(2),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathonPassato = hackathonService.creaHackathon(organizzatore, request);

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.iscriviTeam(hackathonPassato, team, leader.getId()));
    }

    @Test
    void testIscriviTeamParametriNull() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Null Test",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.iscriviTeam(null, team, leader.getId()));

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.iscriviTeam(hackathon, null, leader.getId()));
    }

    @Test
    void testAssegnaGiudiceSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Giudice Test",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice = utenteRepository.save(giudice);

        Hackathon aggiornato = hackathonService.assegnaGiudice(hackathon.getId(), giudice.getId(), organizzatore.getId());

        assertNotNull(aggiornato.getGiudiceId());
        assertEquals(giudice.getId(), aggiornato.getGiudiceId());
        assertTrue(aggiornato.hasGiudice());
        assertTrue(giudice.hasRuolo(Ruolo.GIUDICE));
    }

    @Test
    void testAssegnaGiudiceNonOrganizzatore() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Giudice Non Org",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice = utenteRepository.save(giudice);
        final Long giudiceId = giudice.getId();

        Utente nonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        nonOrganizzatore = utenteRepository.save(nonOrganizzatore);
        final Long nonOrganizzatoreId = nonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), giudiceId, nonOrganizzatoreId));
    }

    @Test
    void testAssegnaGiudiceGiaAssegnato() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Doppio Giudice",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente giudice1 = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice1 = utenteRepository.save(giudice1);
        Utente giudice2 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        giudice2 = utenteRepository.save(giudice2);
        final Long giudice2Id = giudice2.getId();

        hackathonService.assegnaGiudice(hackathon.getId(), giudice1.getId(), organizzatore.getId());

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), giudice2Id, organizzatore.getId()));
    }

    @Test
    void testAssegnaGiudiceIdNull() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Giudice Null",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.assegnaGiudice(hackathon.getId(), null, organizzatore.getId()));
    }

    @Test
    void testRimuoviGiudiceSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Rimuovi Giudice",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice = utenteRepository.save(giudice);

        hackathonService.assegnaGiudice(hackathon.getId(), giudice.getId(), organizzatore.getId());

        Hackathon aggiornato = hackathonService.rimuoviGiudice(hackathon.getId(), organizzatore.getId());

        assertNull(aggiornato.getGiudiceId());
        assertFalse(aggiornato.hasGiudice());
    }

    @Test
    void testRimuoviGiudiceNonOrganizzatore() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Rimuovi Giudice Non Org",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice = utenteRepository.save(giudice);
        hackathonService.assegnaGiudice(hackathon.getId(), giudice.getId(), organizzatore.getId());

        Utente nonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        nonOrganizzatore = utenteRepository.save(nonOrganizzatore);
        final Long nonOrganizzatoreId = nonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.rimuoviGiudice(hackathon.getId(), nonOrganizzatoreId));
    }

    @Test
    void testRimuoviGiudiceNonAssegnato() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Senza Giudice",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.rimuoviGiudice(hackathon.getId(), organizzatore.getId()));
    }

    @Test
    void testAggiungiMembroStaffSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Staff Test",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente staff = new Utente("Marco", "Neri", "marco@example.com", "password");
        staff = utenteRepository.save(staff);

        Hackathon aggiornato = hackathonService.aggiungiMembroStaff(hackathon.getId(), staff.getId(), organizzatore.getId());

        assertTrue(aggiornato.checkStaff(staff.getId()));
        assertTrue(staff.hasRuolo(Ruolo.MEMBRO_STAFF));
    }

    @Test
    void testAggiungiMembroStaffNonOrganizzatore() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Staff Non Org",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente staff = new Utente("Marco", "Neri", "marco@example.com", "password");
        staff = utenteRepository.save(staff);
        final Long staffId = staff.getId();

        Utente nonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        nonOrganizzatore = utenteRepository.save(nonOrganizzatore);
        final Long nonOrganizzatoreId = nonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.aggiungiMembroStaff(hackathon.getId(), staffId, nonOrganizzatoreId));
    }

    @Test
    void testRimuoviMembroStaffSuccess() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Staff Remove",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente staff = new Utente("Marco", "Neri", "marco@example.com", "password");
        staff = utenteRepository.save(staff);

        hackathonService.aggiungiMembroStaff(hackathon.getId(), staff.getId(), organizzatore.getId());
        Hackathon aggiornato = hackathonService.rimuoviMembroStaff(hackathon.getId(), staff.getId(), organizzatore.getId());

        assertFalse(aggiornato.checkStaff(staff.getId()));
    }

    @Test
    void testRimuoviMembroStaffNonOrganizzatore() {
        HackathonRequest request = new HackathonRequest(
                "Hackathon Staff Remove Non Org",
                "Description",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                LocalDate.now().plusMonths(1),
                "Milano",
                "Rules",
                5000.0,
                5
        );

        Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);

        Utente staff = new Utente("Marco", "Neri", "marco@example.com", "password");
        staff = utenteRepository.save(staff);
        final Long staffId = staff.getId();

        hackathonService.aggiungiMembroStaff(hackathon.getId(), staffId, organizzatore.getId());

        Utente nonOrganizzatore = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        nonOrganizzatore = utenteRepository.save(nonOrganizzatore);
        final Long nonOrganizzatoreId = nonOrganizzatore.getId();

        assertThrows(IllegalArgumentException.class,
                () -> hackathonService.rimuoviMembroStaff(hackathon.getId(), staffId, nonOrganizzatoreId));
    }
}
