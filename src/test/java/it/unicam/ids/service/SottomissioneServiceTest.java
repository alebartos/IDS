package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneServiceTest {

    private SottomissioneService sottomissioneService;
    private SottomissioneRepository sottomissioneRepository;
    private TeamRepository teamRepository;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private TeamService teamService;
    private HackathonService hackathonService;

    private Team team;
    private Hackathon hackathon;
    private Utente leader;
    private Utente organizzatore;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
        teamRepository = new TeamRepository();
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();

        teamService = new TeamService(teamRepository, utenteRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository);
        sottomissioneService = new SottomissioneService(sottomissioneRepository, teamRepository, hackathonRepository);

        // Crea organizzatore
        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        // Crea leader del team
        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        // Crea team
        TeamRequest teamRequest = new TeamRequest("Team Alpha", "Test team", leader.getId());
        team = teamService.creaTeam(teamRequest);

        // Crea hackathon IN_CORSO
        HackathonRequest hackathonRequest = new HackathonRequest(
                "Hackathon Test",
                "Description",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                LocalDate.now().minusDays(2),
                "Milano",
                "Rules",
                1000.0,
                5
        );
        hackathon = hackathonService.creaHackathon(organizzatore, hackathonRequest);
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathonRepository.save(hackathon);
    }

    @Test
    void testGestisciBozzaSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");

        Sottomissione sottomissione = sottomissioneService.gestisciBozza(
                team.getId(), hackathon.getId(), dati, leader.getId());

        assertNotNull(sottomissione);
        assertNotNull(sottomissione.getId());
        assertNotNull(sottomissione.getDatiProgetto());
        assertEquals("Descrizione", sottomissione.getDatiProgetto().getDescrizione());
        assertEquals("https://github.com/test", sottomissione.getDatiProgetto().getLinkRepository());
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
        assertEquals(team.getId(), sottomissione.getTeamId());
        assertEquals(hackathon.getId(), sottomissione.getHackathonId());
    }

    @Test
    void testGestisciBozzaNonLeader() {
        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();

        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.gestisciBozza(team.getId(), hackathon.getId(), dati, nonLeaderId));
    }

    @Test
    void testGestisciBozzaHackathonNonInCorso() {
        hackathon.setStato(StatoHackathon.IN_ISCRIZIONE);
        hackathonRepository.save(hackathon);

        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.gestisciBozza(team.getId(), hackathon.getId(), dati, leader.getId()));
    }

    @Test
    void testGestisciBozzaAggiornaBozzaEsistente() {
        DatiProgetto dati1 = new DatiProgetto("Titolo V1", "Descrizione V1", "https://github.com/v1");
        Sottomissione bozza = sottomissioneService.gestisciBozza(
                team.getId(), hackathon.getId(), dati1, leader.getId());

        DatiProgetto dati2 = new DatiProgetto("Titolo V2", "Descrizione V2", "https://github.com/v2");
        Sottomissione aggiornata = sottomissioneService.gestisciBozza(
                team.getId(), hackathon.getId(), dati2, leader.getId());

        assertEquals(bozza.getId(), aggiornata.getId());
        assertEquals("Descrizione V2", aggiornata.getDatiProgetto().getDescrizione());
        assertEquals(StatoSottomissione.BOZZA, aggiornata.getStato());
    }

    @Test
    void testElaboraSottomissioneSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo Finale", "Descrizione finale", "https://github.com/final");

        Sottomissione sottomissione = sottomissioneService.elaboraSottomissione(
                team.getId(), hackathon.getId(), dati, leader.getId());

        assertNotNull(sottomissione);
        assertEquals("Descrizione finale", sottomissione.getDatiProgetto().getDescrizione());
        assertEquals(StatoSottomissione.CONSEGNATA, sottomissione.getStato());
    }

    @Test
    void testElaboraSottomissioneNonLeader() {
        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();

        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.elaboraSottomissione(team.getId(), hackathon.getId(), dati, nonLeaderId));
    }

    @Test
    void testElaboraSottomissioneGiaConsegnata() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");

        sottomissioneService.elaboraSottomissione(team.getId(), hackathon.getId(), dati, leader.getId());

        DatiProgetto dati2 = new DatiProgetto("Titolo 2", "Descrizione 2", "https://github.com/test2");

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.elaboraSottomissione(team.getId(), hackathon.getId(), dati2, leader.getId()));
    }

    @Test
    void testGestisciBozzaDopoConsegna() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");
        sottomissioneService.elaboraSottomissione(team.getId(), hackathon.getId(), dati, leader.getId());

        DatiProgetto datiModifica = new DatiProgetto("Titolo Modifica", "Modifica", "https://github.com/modifica");

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.gestisciBozza(team.getId(), hackathon.getId(), datiModifica, leader.getId()));
    }

    @Test
    void testGetSottomissione() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");
        Sottomissione creata = sottomissioneService.gestisciBozza(
                team.getId(), hackathon.getId(), dati, leader.getId());

        Sottomissione trovata = sottomissioneService.getSottomissione(creata.getId());

        assertNotNull(trovata);
        assertEquals(creata.getId(), trovata.getId());
    }

    @Test
    void testGetSottomissioneNonTrovata() {
        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.getSottomissione(999L));
    }

    @Test
    void testGetSottomissioneByTeamAndHackathon() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");
        Sottomissione creata = sottomissioneService.gestisciBozza(
                team.getId(), hackathon.getId(), dati, leader.getId());

        Sottomissione trovata = sottomissioneService.getSottomissioneByTeamAndHackathon(
                team.getId(), hackathon.getId());

        assertNotNull(trovata);
        assertEquals(creata.getId(), trovata.getId());
    }

    @Test
    void testGetSottomissioniByHackathon() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Descrizione", "https://github.com/test");
        sottomissioneService.gestisciBozza(team.getId(), hackathon.getId(), dati, leader.getId());

        // Crea un altro team e sottomissione
        Utente leader2 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        leader2 = utenteRepository.save(leader2);
        TeamRequest teamRequest2 = new TeamRequest("Team Beta", "Test team 2", leader2.getId());
        Team team2 = teamService.creaTeam(teamRequest2);

        DatiProgetto dati2 = new DatiProgetto("Titolo 2", "Descrizione 2", "https://github.com/test2");
        sottomissioneService.gestisciBozza(team2.getId(), hackathon.getId(), dati2, leader2.getId());

        var sottomissioni = sottomissioneService.getSottomissioniByHackathon(hackathon.getId());

        assertEquals(2, sottomissioni.size());
    }
}
