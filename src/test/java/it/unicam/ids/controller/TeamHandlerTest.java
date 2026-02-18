package it.unicam.ids.controller;

import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamHandlerTest {

    private TeamHandler teamHandler;
    private TeamService teamService;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private InvitoRepository invitoRepository;
    private Utente leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        invitoRepository = new InvitoRepository();
        HackathonRepository hackathonRepository = new HackathonRepository();
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        teamHandler = new TeamHandler(teamService);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.add(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        Result<Team> response = teamHandler.creaTeam("Team Alpha", leader.getId());

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("Team Alpha", response.getData().getNome());
    }

    @Test
    void testCreaTeamNomeDuplicato() {
        teamHandler.creaTeam("Team Dup", leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi@example.com", "pass");
        leader2 = utenteRepository.add(leader2);

        Result<Team> response = teamHandler.creaTeam("Team Dup", leader2.getId());
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void testRimuoviMembroSuccess() {
        Team team = teamService.createTeam("Team Rim", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        membro.getRuoli().add(it.unicam.ids.model.Ruolo.MEMBRO_TEAM);
        utenteRepository.modifyRecord(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        Result<Team> response = teamHandler.rimuoviMembro(membro.getId(), leader.getId());
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testRimuoviMembroNonAppartenente() {
        teamService.createTeam("Team Rim NM", leader.getId());

        Utente estraneo = new Utente("Test", "User", "test@example.com", "pass");
        estraneo = utenteRepository.add(estraneo);

        Result<Team> response = teamHandler.rimuoviMembro(estraneo.getId(), leader.getId());
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void testNominaViceleaderSuccess() {
        Team team = teamService.createTeam("Team Vice H", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        Result<Team> response = teamHandler.nominaViceleader(leader.getId(), membro.getId());
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals(membro.getId(), response.getData().getViceleaderId());
    }

    @Test
    void testNominaViceleaderNonMembro() {
        teamService.createTeam("Team Vice HNM", leader.getId());

        Utente estraneo = new Utente("Test", "User", "test@example.com", "pass");
        estraneo = utenteRepository.add(estraneo);

        Result<Team> response = teamHandler.nominaViceleader(leader.getId(), estraneo.getId());
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void testRevocaViceleaderSuccess() {
        Team team = teamService.createTeam("Team Rev", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        teamService.nominaViceleader(leader.getId(), membro.getId());

        Result<Team> response = teamHandler.revocaViceleader(leader.getId(), membro.getId());
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertNull(response.getData().getViceleaderId());
    }

    @Test
    void testRevocaViceleaderNonViceleader() {
        Team team = teamService.createTeam("Team Rev NV", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        Result<Team> response = teamHandler.revocaViceleader(leader.getId(), membro.getId());
        assertFalse(response.isSuccess());
        assertEquals(400, response.getStatusCode());
    }
}
