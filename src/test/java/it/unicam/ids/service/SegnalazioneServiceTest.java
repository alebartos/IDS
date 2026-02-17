package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SegnalazioneServiceTest {

    private SegnalazioneService segnalazioneService;
    private SegnalazioneRepository segnalazioneRepository;
    private HackathonRepository hackathonRepository;
    private UtenteRepository utenteRepository;
    private HackathonService hackathonService;

    private Utente organizzatore;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        segnalazioneRepository = new SegnalazioneRepository();
        hackathonRepository = new HackathonRepository();
        utenteRepository = new UtenteRepository();
        TeamRepository teamRepository = new TeamRepository();
        InvitoRepository invitoRepository = new InvitoRepository();

        TeamService teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService);
        segnalazioneService = new SegnalazioneService(segnalazioneRepository, hackathonRepository, utenteRepository);

        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                5, 1000.0, organizzatore.getId());
    }

    @Test
    void testSegnalaSuccess() {
        assertDoesNotThrow(() ->
                segnalazioneService.segnala(1L, "Comportamento scorretto", hackathon.getId()));

        Segnalazione segnalazione = segnalazioneRepository.findById(1L).orElseThrow();
        assertEquals("Comportamento scorretto", segnalazione.getDescrizione());
        assertEquals(1L, segnalazione.getTeamId());
        assertEquals(hackathon.getId(), segnalazione.getHackathonId());
    }

    @Test
    void testSegnalaHackathonNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> segnalazioneService.segnala(1L, "Descrizione", 9999L));
    }

    @Test
    void testInviaMail() {
        assertDoesNotThrow(() ->
                segnalazioneService.inviaMail("test@example.com", "Test oggetto"));
    }
}
