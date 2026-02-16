package it.unicam.ids.service;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamServiceTest {

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
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        Team team = teamService.createTeam("Team Alpha", leader.getId());

        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals("Team Alpha", team.getNome());
        assertEquals(leader.getId(), team.getLeaderId());
        // Verifica che il leader abbia il ruolo LEADER
        assertTrue(leader.getRuoli().contains(Ruolo.LEADER));
    }

    @Test
    void testCreaTeamDuplicateNome() {
        teamService.createTeam("Team Duplicate", leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        leader2 = utenteRepository.save(leader2);
        final Long leader2Id = leader2.getId();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.createTeam("Team Duplicate", leader2Id));
    }

    @Test
    void testCreaTeamLeaderGiaEsistente() {
        teamService.createTeam("Team One", leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> teamService.createTeam("Team Two", leader.getId()));
    }

    @Test
    void testCreaTeamUtenteNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> teamService.createTeam("Team Invalid", 999L));
    }

    @Test
    void testFindById() {
        Team team = teamService.createTeam("Team FindById", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.save(membro);
        team.getMembri().add(membro.getId());
        teamRepository.save(team);

        assertTrue(teamService.findById(membro.getId()));
        assertFalse(teamService.findById(9999L));
    }
}
