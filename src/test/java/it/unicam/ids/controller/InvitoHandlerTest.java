package it.unicam.ids.controller;

import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.InvitoService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvitoHandlerTest {

    private InvitoHandler invitoHandler;
    private UtenteRepository utenteRepository;
    private TeamRepository teamRepository;
    private InvitoRepository invitoRepository;
    private TeamService teamService;

    private Utente leader;
    private Utente destinatario;
    private Team team;

    @BeforeEach
    void setUp() {
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        invitoRepository = new InvitoRepository();

        HackathonRepository hackathonRepository = new HackathonRepository();
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        InvitoService invitoService = new InvitoService(utenteRepository, teamRepository, invitoRepository);
        invitoHandler = new InvitoHandler(invitoService);

        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.add(leader);
        team = teamService.createTeam("Team Test", leader.getId());

        destinatario = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        destinatario = utenteRepository.add(destinatario);
    }

    @Test
    void testInvitaMembroSuccess() {
        Result<String> result = invitoHandler.invitaMembro(
                "anna@example.com", team.getId(), leader.getId());

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testInvitaMembroNonLeader() {
        Utente nonLeader = new Utente("Luigi", "Verdi", "luigi@example.com", "pass");
        nonLeader = utenteRepository.add(nonLeader);

        Result<String> result = invitoHandler.invitaMembro(
                "anna@example.com", team.getId(), nonLeader.getId());

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testInvitaMembroEmailNonTrovata() {
        Result<String> result = invitoHandler.invitaMembro(
                "nessuno@example.com", team.getId(), leader.getId());

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testInvitaMembroDuplicato() {
        invitoHandler.invitaMembro("anna@example.com", team.getId(), leader.getId());

        Result<String> result = invitoHandler.invitaMembro(
                "anna@example.com", team.getId(), leader.getId());

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testGestisciInvitoAccettato() {
        invitoHandler.invitaMembro("anna@example.com", team.getId(), leader.getId());
        Long invitoId = invitoRepository.findAll().get(0).getId();

        Result<String> result = invitoHandler.gestisciInvito(invitoId, "ACCETTATO");

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testGestisciInvitoRifiutato() {
        invitoHandler.invitaMembro("anna@example.com", team.getId(), leader.getId());
        Long invitoId = invitoRepository.findAll().get(0).getId();

        Result<String> result = invitoHandler.gestisciInvito(invitoId, "RIFIUTATO");

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testGestisciInvitoNonTrovato() {
        Result<String> result = invitoHandler.gestisciInvito(999L, "ACCETTATO");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testGestisciInvitoRispostaNonValida() {
        invitoHandler.invitaMembro("anna@example.com", team.getId(), leader.getId());
        Long invitoId = invitoRepository.findAll().get(0).getId();

        Result<String> result = invitoHandler.gestisciInvito(invitoId, "INVALIDA");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
