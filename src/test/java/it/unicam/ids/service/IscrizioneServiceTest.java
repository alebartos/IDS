package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IscrizioneServiceTest {

    private IscrizioneService iscrizioneService;
    private HackathonRepository hackathonRepository;
    private TeamRepository teamRepository;
    private UtenteRepository utenteRepository;
    private HackathonService hackathonService;
    private TeamService teamService;

    private Utente organizzatore;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        hackathonRepository = new HackathonRepository();
        teamRepository = new TeamRepository();
        utenteRepository = new UtenteRepository();
        InvitoRepository invitoRepository = new InvitoRepository();

        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        iscrizioneService = new IscrizioneService(teamRepository, hackathonRepository, utenteRepository);

        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        Utente leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.add(leader);
        team = teamService.createTeam("Team Test", leader.getId());

        Utente membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro = utenteRepository.add(membro);
        team.getMembri().add(membro.getId());
        teamRepository.modifyRecord(team);

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(30),
                LocalDate.now().plusDays(10),
                5, 1000.0, organizzatore.getId());
    }

    @Test
    void testGetDettagliHackathon() {
        Hackathon trovato = iscrizioneService.getDettagliHackathon(hackathon.getId());
        assertNotNull(trovato);
        assertEquals("Hackathon Test", trovato.getNome());
    }

    @Test
    void testGetDettagliHackathonNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.getDettagliHackathon(999L));
    }

    @Test
    void testCheckMaxTeamSuccess() {
        assertDoesNotThrow(() -> iscrizioneService.checkMaxTeam(team.getId(), hackathon.getId()));
    }

    @Test
    void testCheckMaxTeamGiaIscritto() {
        hackathon.getTeamIds().add(team.getId());
        hackathonRepository.modifyRecord(hackathon);

        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.checkMaxTeam(team.getId(), hackathon.getId()));
    }

    @Test
    void testVerificaMaxMembriTrue() {
        assertTrue(iscrizioneService.verificaMaxMembri(5, 3));
    }

    @Test
    void testVerificaMaxMembriFalse() {
        assertFalse(iscrizioneService.verificaMaxMembri(2, 5));
    }

    @Test
    void testVerificaMaxMembriUguali() {
        assertTrue(iscrizioneService.verificaMaxMembri(3, 3));
    }

    @Test
    void testSelezionaPartecipanti() {
        Map<String, Object> result = iscrizioneService.selezionaPartecipanti(team.getId(), hackathon.getId());

        assertNotNull(result);
        assertNotNull(result.get("membri"));
        assertNotNull(result.get("maxMembriTeam"));
    }

    @Test
    void testSelezionaPartecipantiTeamNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.selezionaPartecipanti(999L, hackathon.getId()));
    }

    @Test
    void testIscriviTeamSuccess() {
        List<Long> partecipanti = List.of(team.getMembri().get(0));

        assertDoesNotThrow(() ->
                iscrizioneService.iscriviTeam(team.getId(), hackathon.getId(), partecipanti));

        Hackathon aggiornato = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        assertTrue(aggiornato.getTeamIds().contains(team.getId()));
    }

    @Test
    void testIscriviTeamGiaIscritto() {
        hackathon.getTeamIds().add(team.getId());
        hackathonRepository.modifyRecord(hackathon);

        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.iscriviTeam(team.getId(), hackathon.getId(), List.of(1L)));
    }

    @Test
    void testIscriviTeamHackathonNonInIscrizione() {
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathonRepository.modifyRecord(hackathon);

        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.iscriviTeam(team.getId(), hackathon.getId(), List.of(1L)));
    }

    @Test
    void testIscriviTeamTroppiPartecipanti() {
        List<Long> troppi = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.iscriviTeam(team.getId(), hackathon.getId(), troppi));
    }

    @Test
    void testCheckValiditàSuccess() {
        assertDoesNotThrow(() -> iscrizioneService.checkValidità(hackathon));
    }

    @Test
    void testCheckValiditàNonInIscrizione() {
        hackathon.setStato(StatoHackathon.IN_CORSO);
        assertThrows(IllegalArgumentException.class,
                () -> iscrizioneService.checkValidità(hackathon));
    }
}
