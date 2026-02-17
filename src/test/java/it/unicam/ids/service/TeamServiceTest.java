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
        leader = utenteRepository.add(leader);
    }

    @Test
    void testCreaTeamSuccess() {
        Team team = teamService.createTeam("Team Alpha", leader.getId());

        assertNotNull(team);
        assertNotNull(team.getId());
        assertEquals("Team Alpha", team.getNome());
        assertEquals(leader.getId(), team.getLeaderId());
        assertTrue(leader.getRuoli().contains(Ruolo.LEADER));
    }

    @Test
    void testCreaTeamDuplicateNome() {
        teamService.createTeam("Team Duplicate", leader.getId());

        Utente leader2 = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        leader2 = utenteRepository.add(leader2);
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
    void testRimuoviMembroSuccess() {
        Team team = teamService.createTeam("Team Rimuovi", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        utenteRepository.modifyRecord(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        Team aggiornato = teamService.rimuoviMembro(membro.getId(), leader.getId());
        assertFalse(aggiornato.getMembri().contains(membro.getId()));
    }

    @Test
    void testRimuoviMembroSeStesso() {
        teamService.createTeam("Team Self", leader.getId());

        assertThrows(IllegalArgumentException.class,
                () -> teamService.rimuoviMembro(leader.getId(), leader.getId()));
    }

    @Test
    void testRimuoviMembroNonAppartenente() {
        teamService.createTeam("Team No Member", leader.getId());

        Utente estraneo = new Utente("Test", "User", "test@example.com", "pass");
        estraneo = utenteRepository.add(estraneo);
        final Long estraneoId = estraneo.getId();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.rimuoviMembro(estraneoId, leader.getId()));
    }

    @Test
    void testNominaViceleaderSuccess() {
        Team team = teamService.createTeam("Team Vice", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        Team aggiornato = teamService.nominaViceleader(leader.getId(), membro.getId());
        assertEquals(membro.getId(), aggiornato.getViceleaderId());
        assertTrue(membro.getRuoli().contains(Ruolo.VICELEADER));
    }

    @Test
    void testNominaViceleaderGiaEsistente() {
        Team team = teamService.createTeam("Team Vice Dup", leader.getId());

        Utente m1 = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        m1 = utenteRepository.add(m1);
        team.getMembri().add(m1.getId());

        Utente m2 = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        m2 = utenteRepository.add(m2);
        team.getMembri().add(m2.getId());
        teamRepository.modifyRecord(team);

        teamService.nominaViceleader(leader.getId(), m1.getId());
        final Long m2Id = m2.getId();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.nominaViceleader(leader.getId(), m2Id));
    }

    @Test
    void testNominaViceleaderNonMembro() {
        teamService.createTeam("Team Vice NM", leader.getId());

        Utente estraneo = new Utente("Test", "User", "test@example.com", "pass");
        estraneo = utenteRepository.add(estraneo);
        final Long estraneoId = estraneo.getId();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.nominaViceleader(leader.getId(), estraneoId));
    }

    @Test
    void testRemoveViceleaderSuccess() {
        Team team = teamService.createTeam("Team RemVice", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        teamService.nominaViceleader(leader.getId(), membro.getId());
        Team aggiornato = teamService.removeViceleader(leader.getId(), membro.getId());

        assertNull(aggiornato.getViceleaderId());
        assertFalse(membro.getRuoli().contains(Ruolo.VICELEADER));
    }

    @Test
    void testRemoveViceleaderNonViceleader() {
        Team team = teamService.createTeam("Team RemVice NV", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);
        final Long membroId = membro.getId();

        assertThrows(IllegalArgumentException.class,
                () -> teamService.removeViceleader(leader.getId(), membroId));
    }

    @Test
    void testFindById() {
        Team team = teamService.createTeam("Team FindById", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        assertTrue(teamService.findById(membro.getId()));
        assertFalse(teamService.findById(9999L));
    }
}
