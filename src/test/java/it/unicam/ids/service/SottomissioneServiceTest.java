package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SottomissioneServiceTest {

    @Autowired
    private SottomissioneService sottomissioneService;

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private SottomissioneRepository sottomissioneRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    private Team team;
    private Hackathon hackathon;
    private Utente leader;
    private Utente organizzatore;

    @BeforeEach
    void setUp() {
        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore.getRuoli().add(Ruolo.MEMBRO_STAFF);
        organizzatore = utenteRepository.save(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        team = teamService.createTeam("Team Alpha", leader.getId());

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathonRepository.save(hackathon);
    }

    @Test
    void testGestisciBozzeSuccess() {
        Sottomissione sottomissione = sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId());

        assertNotNull(sottomissione);
        assertNotNull(sottomissione.getId());
        assertEquals(StatoSottomissione.BOZZA, sottomissione.getStato());
        assertEquals(team.getId(), sottomissione.getTeamId());
        assertEquals(hackathon.getId(), sottomissione.getHackathonId());
    }

    @Test
    void testGestisciBozzeRestituisceBozzaEsistente() {
        Sottomissione bozza = sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId());
        Sottomissione stessa = sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId());

        assertEquals(bozza.getId(), stessa.getId());
    }

    @Test
    void testGestisciBozzeDopoConsegna() {
        Sottomissione bozza = sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId());
        bozza.setStato(StatoSottomissione.CONSEGNATA);
        sottomissioneRepository.save(bozza);

        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId()));
    }

    @Test
    void testCheckValiditaLinkSuccess() {
        assertDoesNotThrow(() -> sottomissioneService.checkValiditàLink("https://github.com/test"));
        assertDoesNotThrow(() -> sottomissioneService.checkValiditàLink("http://example.com"));
    }

    @Test
    void testCheckValiditaLinkInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.checkValiditàLink(null));
        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.checkValiditàLink(""));
        assertThrows(IllegalArgumentException.class,
                () -> sottomissioneService.checkValiditàLink("ftp://invalid.com"));
    }

    @Test
    void testCreaDTO() {
        DatiProgetto dto = sottomissioneService.creaDTO("Titolo", "Descrizione", "https://github.com/test");

        assertNotNull(dto);
        assertEquals("Titolo", dto.getTitolo());
        assertEquals("Descrizione", dto.getDescrizione());
        assertEquals("https://github.com/test", dto.getLinkRepository());
    }
}
