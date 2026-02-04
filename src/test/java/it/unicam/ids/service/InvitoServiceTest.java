package it.unicam.ids.service;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Invito;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvitoServiceTest {

    private InvitoService invitoService;
    private InvitoRepository invitoRepository;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private TeamService teamService;

    private Team team;
    private Utente leader;
    private Utente destinatario;

    @BeforeEach
    void setUp() {
        invitoRepository = new InvitoRepository();
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();

        invitoService = new InvitoService(invitoRepository, teamRepository, utenteRepository);
        teamService = new TeamService(teamRepository, utenteRepository);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        destinatario = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password456");
        destinatario = utenteRepository.save(destinatario);

        TeamRequest teamRequest = new TeamRequest("Team Test", "Descrizione test", leader.getId());
        team = teamService.creaTeam(teamRequest);
    }

    @Test
    void testInvitaMembroSuccess() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        assertNotNull(invito);
        assertNotNull(invito.getId());
        assertEquals(team.getId(), invito.getTeam().getId());
        assertEquals(destinatario.getId(), invito.getDestinatarioId());
        assertEquals(StatoInvito.PENDING, invito.getStato());
    }

    @Test
    void testInvitaMembroNonLeader() {
        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro(team.getId(), destinatario.getId(), nonLeaderId));
    }

    @Test
    void testInvitaMembroTeamNonTrovato() {
        Long teamIdNonEsistente = 999L;

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro(teamIdNonEsistente, destinatario.getId(), leader.getId()));
    }

    @Test
    void testInvitaMembroUtenteNonTrovato() {
        Long utenteIdNonEsistente = 9999L;

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro(team.getId(), utenteIdNonEsistente, leader.getId()));
    }

    @Test
    void testInvitaMembroDuplicato() {
        invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId()));
    }

    @Test
    void testGetInvito() {
        Invito creato = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        Invito trovato = invitoService.getInvito(creato.getId());

        assertNotNull(trovato);
        assertEquals(creato.getId(), trovato.getId());
    }

    @Test
    void testGetInvitoNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> invitoService.getInvito(999L));
    }

    @Test
    void testAccettaInvito() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        invitoService.accettaInvito(invito.getId(), destinatario.getId());

        Invito aggiornato = invitoService.getInvito(invito.getId());
        assertEquals(StatoInvito.ACCEPTED, aggiornato.getStato());

        // Verifica che il destinatario sia stato aggiunto al team
        Team teamAggiornato = teamService.getDettagliTeam(team.getId());
        assertTrue(teamAggiornato.hasMembro(destinatario.getId()));

        // Verifica che il destinatario abbia il ruolo MEMBRO_TEAM
        assertTrue(destinatario.hasRuolo(Ruolo.MEMBRO_TEAM));
    }

    @Test
    void testAccettaInvitoNonDestinatario() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        Utente altroUtente = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        altroUtente = utenteRepository.save(altroUtente);
        final Long altroUtenteId = altroUtente.getId();

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.accettaInvito(invito.getId(), altroUtenteId));
    }

    @Test
    void testAccettaInvitoGiaGestito() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        invitoService.accettaInvito(invito.getId(), destinatario.getId());

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.accettaInvito(invito.getId(), destinatario.getId()));
    }

    @Test
    void testRifiutaInvito() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        invitoService.rifiutaInvito(invito.getId(), destinatario.getId());

        Invito aggiornato = invitoService.getInvito(invito.getId());
        assertEquals(StatoInvito.REJECTED, aggiornato.getStato());
    }

    @Test
    void testRifiutaInvitoNonDestinatario() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        Utente altroUtente = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        altroUtente = utenteRepository.save(altroUtente);
        final Long altroUtenteId = altroUtente.getId();

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.rifiutaInvito(invito.getId(), altroUtenteId));
    }

    @Test
    void testGestisciInvitoAccepted() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        invitoService.gestisciInvito(invito.getId(), "ACCEPTED", destinatario.getId());

        Invito aggiornato = invitoService.getInvito(invito.getId());
        assertEquals(StatoInvito.ACCEPTED, aggiornato.getStato());
    }

    @Test
    void testGestisciInvitoRejected() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        invitoService.gestisciInvito(invito.getId(), "REJECTED", destinatario.getId());

        Invito aggiornato = invitoService.getInvito(invito.getId());
        assertEquals(StatoInvito.REJECTED, aggiornato.getStato());
    }

    @Test
    void testGestisciInvitoRispostaNonValida() {
        Invito invito = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.gestisciInvito(invito.getId(), "INVALID", destinatario.getId()));
    }

    @Test
    void testGetInvitiPendentiPerUtente() {
        invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password789");
        leader2 = utenteRepository.save(leader2);
        TeamRequest teamRequest2 = new TeamRequest("Team 2", "Descrizione 2", leader2.getId());
        Team team2 = teamService.creaTeam(teamRequest2);
        invitoService.invitaMembro(team2.getId(), destinatario.getId(), leader2.getId());

        List<Invito> inviti = invitoService.getInvitiPendentiPerUtente(destinatario.getId());

        assertEquals(2, inviti.size());
    }

    @Test
    void testChiudiAltriInvitiQuandoAccettato() {
        Invito invito1 = invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password789");
        leader2 = utenteRepository.save(leader2);
        TeamRequest teamRequest2 = new TeamRequest("Team 2", "Descrizione 2", leader2.getId());
        Team team2 = teamService.creaTeam(teamRequest2);
        Invito invito2 = invitoService.invitaMembro(team2.getId(), destinatario.getId(), leader2.getId());

        invitoService.accettaInvito(invito1.getId(), destinatario.getId());

        Invito primoInvito = invitoService.getInvito(invito1.getId());
        Invito secondoInvito = invitoService.getInvito(invito2.getId());

        assertEquals(StatoInvito.ACCEPTED, primoInvito.getStato());
        assertEquals(StatoInvito.REJECTED, secondoInvito.getStato());
    }

    @Test
    void testGetInvitiPerTeam() {
        Utente dest2 = new Utente("Paolo", "Neri", "paolo@example.com", "password");
        dest2 = utenteRepository.save(dest2);
        Utente dest3 = new Utente("Marco", "Gialli", "marco@example.com", "password");
        dest3 = utenteRepository.save(dest3);

        invitoService.invitaMembro(team.getId(), destinatario.getId(), leader.getId());
        invitoService.invitaMembro(team.getId(), dest2.getId(), leader.getId());
        invitoService.invitaMembro(team.getId(), dest3.getId(), leader.getId());

        List<Invito> inviti = invitoService.getInvitiPerTeam(team.getId());

        assertEquals(3, inviti.size());
    }
}
