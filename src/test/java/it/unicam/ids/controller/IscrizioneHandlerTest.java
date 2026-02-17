package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.IscrizioneService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IscrizioneHandlerTest {

    private IscrizioneHandler iscrizioneHandler;
    private IscrizioneService iscrizioneService;
    private HackathonService hackathonService;
    private TeamService teamService;
    private HackathonRepository hackathonRepository;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private InvitoRepository invitoRepository;

    private Hackathon hackathon;
    private Team team;
    private Utente leader;
    private Utente membro1;
    private Utente membro2;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        invitoRepository = new InvitoRepository();

        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService);
        iscrizioneService = new IscrizioneService(teamRepository, hackathonRepository);
        iscrizioneHandler = new IscrizioneHandler(iscrizioneService);

        Utente organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        hackathon = hackathonService.createHackathon(
                "Test Hackathon", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                5, 5000.0, organizzatore.getId());
        hackathon.setScadenzaIscrizioni(LocalDate.now().plusMonths(1));
        hackathonRepository.modifyRecord(hackathon);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.add(leader);

        membro1 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro1 = utenteRepository.add(membro1);

        membro2 = new Utente("Paolo", "Neri", "paolo@example.com", "password");
        membro2 = utenteRepository.add(membro2);

        team = teamService.createTeam("Team Alpha", leader.getId());

        team.getMembri().add(membro1.getId());
        team.getMembri().add(membro2.getId());
        teamRepository.modifyRecord(team);
    }

    @Test
    void testIscriviTeamSuccess() {
        Result<String> response = iscrizioneHandler.iscriviTeam(team.getId(), hackathon.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());

        Hackathon aggiornato = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertTrue(aggiornato.getTeamIds().contains(team.getId()));
    }

    @Test
    void testIscriviTeamHackathonNonValido() {
        Utente org = new Utente("Paolo", "Bianchi", "paolo.bianchi@example.com", "pass");
        org.getRuoli().add(Ruolo.ORGANIZZATORE);
        org = utenteRepository.add(org);

        Hackathon hackathonPassato = hackathonService.createHackathon(
                "Passato Hackathon", "Description",
                LocalDate.now().minusMonths(1), LocalDate.now().minusMonths(1).plusDays(3),
                5, 5000.0, org.getId());
        hackathonPassato.setScadenzaIscrizioni(LocalDate.now().minusMonths(2));
        hackathonRepository.modifyRecord(hackathonPassato);

        Result<String> response = iscrizioneHandler.iscriviTeam(team.getId(), hackathonPassato.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testIscriviTeamNotFound() {
        Result<String> response = iscrizioneHandler.iscriviTeam(team.getId(), 99999L);

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testIscriviTeamGiaIscritto() {
        iscrizioneHandler.iscriviTeam(team.getId(), hackathon.getId());

        Result<String> response = iscrizioneHandler.iscriviTeam(team.getId(), hackathon.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testSelezionaPartecipantiGetMembri() {
        Result<Map<String, Object>> response = iscrizioneHandler.selezionaPartecipanti(team.getId(), hackathon.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());

        Map<String, Object> dati = response.getData();
        assertNotNull(dati.get("membri"));
        assertNotNull(dati.get("maxMembriTeam"));
        assertEquals(5, dati.get("maxMembriTeam"));
    }

    @Test
    void testSelezionaPartecipantiConSelezione() {
        List<Long> selected = Arrays.asList(membro1.getId(), membro2.getId());

        Result<String> response = iscrizioneHandler.selezionaPartecipanti(team.getId(), selected, hackathon.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isSuccess());
    }

    @Test
    void testSelezionaPartecipantiSuperaMax() {
        Utente org = new Utente("Marco", "Blu", "marco.blu@example.com", "pass");
        org.getRuoli().add(Ruolo.ORGANIZZATORE);
        org = utenteRepository.add(org);

        Hackathon hackathonPiccolo = hackathonService.createHackathon(
                "Hackathon Piccolo", "Description",
                LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(2).plusDays(3),
                1, 5000.0, org.getId());
        hackathonPiccolo.setScadenzaIscrizioni(LocalDate.now().plusMonths(1));
        hackathonRepository.modifyRecord(hackathonPiccolo);

        List<Long> selected = Arrays.asList(membro1.getId(), membro2.getId());

        Result<String> response = iscrizioneHandler.selezionaPartecipanti(team.getId(), selected, hackathonPiccolo.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }

    @Test
    void testSelezionaPartecipantiTeamNonTrovato() {
        Result<Map<String, Object>> response = iscrizioneHandler.selezionaPartecipanti(99999L, hackathon.getId());

        assertEquals(400, response.getStatusCode());
        assertFalse(response.isSuccess());
    }
}
