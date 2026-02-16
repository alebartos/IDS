package it.unicam.ids.service;

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

        invitoService = new InvitoService(utenteRepository, teamRepository, invitoRepository);
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        destinatario = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password456");
        destinatario = utenteRepository.save(destinatario);

        team = teamService.createTeam("Team Test", leader.getId());
    }

    @Test
    void testInvitaMembroSuccess() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        assertNotNull(invito);
        assertNotNull(invito.getId());
        assertEquals(team.getId(), invito.getTeam().getId());
        assertEquals(destinatario.getId(), invito.getDestinatario().getId());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testInvitaMembroNonLeader() {
        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), nonLeaderId));
    }

    @Test
    void testInvitaMembroTeamNonTrovato() {
        Long teamIdNonEsistente = 999L;

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("anna.bianchi@example.com", teamIdNonEsistente, leader.getId()));
    }

    @Test
    void testInvitaMembroUtenteNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("nonexistent@example.com", team.getId(), leader.getId()));
    }

    @Test
    void testInvitaMembroDuplicato() {
        invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId()));
    }

    @Test
    void testGestisciInvitoAccettato() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        invitoService.gestisciInvito(invito.getId(), "ACCETTATO");

        Invito aggiornato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.ACCETTATO, aggiornato.getStato());

        // Verifica che il destinatario abbia il ruolo MEMBRO_TEAM
        assertTrue(destinatario.getRuoli().contains(Ruolo.MEMBRO_TEAM));

        // Verifica che il membro sia stato aggiunto al team
        Team teamAggiornato = teamRepository.findById(team.getId()).orElseThrow();
        assertTrue(teamAggiornato.getMembri().contains(destinatario.getId()));
    }

    @Test
    void testGestisciInvitoRifiutato() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        invitoService.gestisciInvito(invito.getId(), "RIFIUTATO");

        Invito aggiornato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.RIFIUTATO, aggiornato.getStato());
    }

    @Test
    void testGestisciInvitoRispostaNonValida() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.gestisciInvito(invito.getId(), "INVALID"));
    }

    @Test
    void testGestisciInvitoGiaGestito() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        invitoService.gestisciInvito(invito.getId(), "ACCETTATO");

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.gestisciInvito(invito.getId(), "ACCETTATO"));
    }

    @Test
    void testChiudiAltriInvitiQuandoAccettato() {
        Invito invito1 = invitoService.invitaMembro("anna.bianchi@example.com", team.getId(), leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password789");
        leader2 = utenteRepository.save(leader2);
        Team team2 = teamService.createTeam("Team 2", leader2.getId());
        Invito invito2 = invitoService.invitaMembro("anna.bianchi@example.com", team2.getId(), leader2.getId());

        invitoService.gestisciInvito(invito1.getId(), "ACCETTATO");

        Invito primoInvito = invitoRepository.findById(invito1.getId()).orElseThrow();
        Invito secondoInvito = invitoRepository.findById(invito2.getId()).orElseThrow();

        assertEquals(StatoInvito.ACCETTATO, primoInvito.getStato());
        assertEquals(StatoInvito.RIFIUTATO, secondoInvito.getStato());
    }
}
