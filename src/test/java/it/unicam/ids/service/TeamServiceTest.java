package it.unicam.ids.service;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

    private TeamService teamService;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private Utente leader;

    @BeforeEach
    void setUp() {
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        teamService = new TeamService(teamRepository, utenteRepository);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        TeamRequest request = new TeamRequest("Team Alpha", "Test team", leader.getId());

        Team team = teamService.creaTeam(request);

        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals("Team Alpha", team.getNome());
        assertEquals("Test team", team.getDescrizione());
        assertEquals(leader.getId(), team.getLeader().getId());
        // Verifica che il leader abbia il ruolo LEADER
        assertTrue(leader.hasRuolo(Ruolo.LEADER));
    }

    @Test
    void testCreaTeamDuplicateNome() {
        TeamRequest request1 = new TeamRequest("Team Duplicate", "First", leader.getId());
        teamService.creaTeam(request1);

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        leader2 = utenteRepository.save(leader2);

        TeamRequest request2 = new TeamRequest("Team Duplicate", "Second", leader2.getId());

        assertThrows(IllegalArgumentException.class, () -> teamService.creaTeam(request2));
    }

    @Test
    void testEsisteTeamConNome() {
        TeamRequest request = new TeamRequest("Team Beta", "Test", leader.getId());
        teamService.creaTeam(request);

        assertTrue(teamService.esisteTeamConNome("Team Beta"));
        assertFalse(teamService.esisteTeamConNome("Team NonEsistente"));
    }

    @Test
    void testValidaTeam() {
        Team validTeam = new Team();
        validTeam.setNome("Valid Team");
        validTeam.setDataCreazione(LocalDate.now());
        validTeam.setLeader(leader);

        assertTrue(teamService.validaTeam(validTeam));

        assertFalse(teamService.validaTeam(null));

        Team invalidTeam = new Team();
        invalidTeam.setNome("");
        invalidTeam.setDataCreazione(LocalDate.now());
        invalidTeam.setLeader(leader);
        assertFalse(teamService.validaTeam(invalidTeam));
    }

    @Test
    void testGetDettagliTeam() {
        TeamRequest request = new TeamRequest("Team Gamma", "Details test", leader.getId());
        Team createdTeam = teamService.creaTeam(request);

        Team retrievedTeam = teamService.getDettagliTeam(createdTeam.getId());

        assertNotNull(retrievedTeam);
        assertEquals(createdTeam.getId(), retrievedTeam.getId());
        assertEquals("Team Gamma", retrievedTeam.getNome());
    }

    @Test
    void testGetTeamByLeader() {
        TeamRequest request = new TeamRequest("Team Delta", "Leader test", leader.getId());
        Team createdTeam = teamService.creaTeam(request);

        Team retrievedTeam = teamService.getTeamByLeader(leader);

        assertNotNull(retrievedTeam);
        assertEquals(createdTeam.getId(), retrievedTeam.getId());
    }

    @Test
    void testAggiungiMembroSuccess() {
        TeamRequest request = new TeamRequest("Team Membri", "Test membri", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password789");
        membro = utenteRepository.save(membro);

        // Il leader aggiunge il membro
        Team teamAggiornato = teamService.aggiungiMembro(team.getId(), membro.getId(), leader.getId());

        assertEquals(1, teamAggiornato.getNumeroMembri());
        assertTrue(teamAggiornato.hasMembro(membro.getId()));
        assertTrue(membro.hasRuolo(Ruolo.MEMBRO_TEAM));
    }

    @Test
    void testAggiungiMembroNonLeader() {
        TeamRequest request = new TeamRequest("Team Non Leader", "Test", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password789");
        membro = utenteRepository.save(membro);

        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();
        final Long membroId = membro.getId();

        // Un non-leader tenta di aggiungere un membro
        assertThrows(IllegalArgumentException.class,
                () -> teamService.aggiungiMembro(team.getId(), membroId, nonLeaderId));
    }

    @Test
    void testAggiungiMembroGiaPresente() {
        TeamRequest request = new TeamRequest("Team Membri Dup", "Test membri", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password789");
        membro = utenteRepository.save(membro);
        final Long membroId = membro.getId();

        teamService.aggiungiMembro(team.getId(), membroId, leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> teamService.aggiungiMembro(team.getId(), membroId, leader.getId()));
    }

    @Test
    void testAggiungiMembroNull() {
        TeamRequest request = new TeamRequest("Team Membri Null", "Test membri", leader.getId());
        Team team = teamService.creaTeam(request);

        assertThrows(IllegalArgumentException.class,
                () -> teamService.aggiungiMembro(team.getId(), null, leader.getId()));
    }

    @Test
    void testRimuoviMembroSuccess() {
        TeamRequest request = new TeamRequest("Team Rimuovi", "Test rimuovi", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password789");
        membro = utenteRepository.save(membro);

        teamService.aggiungiMembro(team.getId(), membro.getId(), leader.getId());

        // Il leader rimuove il membro
        Team teamAggiornato = teamService.rimuoviMembro(team.getId(), membro.getId(), leader.getId());

        assertEquals(0, teamAggiornato.getNumeroMembri());
        assertFalse(teamAggiornato.hasMembro(membro.getId()));
    }

    @Test
    void testRimuoviMembroNonLeader() {
        TeamRequest request = new TeamRequest("Team Rimuovi Non Leader", "Test", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password789");
        membro = utenteRepository.save(membro);
        teamService.aggiungiMembro(team.getId(), membro.getId(), leader.getId());

        Utente nonLeader = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        nonLeader = utenteRepository.save(nonLeader);
        final Long nonLeaderId = nonLeader.getId();
        final Long membroId = membro.getId();

        // Un non-leader tenta di rimuovere un membro
        assertThrows(IllegalArgumentException.class,
                () -> teamService.rimuoviMembro(team.getId(), membroId, nonLeaderId));
    }

    @Test
    void testRimuoviMembroNonPresente() {
        TeamRequest request = new TeamRequest("Team Non Presente", "Test", leader.getId());
        Team team = teamService.creaTeam(request);

        assertThrows(IllegalArgumentException.class,
                () -> teamService.rimuoviMembro(team.getId(), 999L, leader.getId()));
    }

    @Test
    void testRimuoviLeader() {
        TeamRequest request = new TeamRequest("Team Leader Remove", "Test", leader.getId());
        Team team = teamService.creaTeam(request);

        assertThrows(IllegalArgumentException.class,
                () -> teamService.rimuoviMembro(team.getId(), leader.getId(), leader.getId()));
    }

    @Test
    void testGetMembriTeam() {
        TeamRequest request = new TeamRequest("Team Get Membri", "Test", leader.getId());
        Team team = teamService.creaTeam(request);

        Utente membro1 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro1 = utenteRepository.save(membro1);
        Utente membro2 = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        membro2 = utenteRepository.save(membro2);

        teamService.aggiungiMembro(team.getId(), membro1.getId(), leader.getId());
        teamService.aggiungiMembro(team.getId(), membro2.getId(), leader.getId());

        List<Utente> membri = teamService.getMembriTeam(team.getId());

        assertEquals(2, membri.size());
    }
}
