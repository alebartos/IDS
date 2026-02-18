package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.SegnalazioneService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SegnalazioneHandlerTest {

    private SegnalazioneHandler segnalazioneHandler;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        SegnalazioneRepository segnalazioneRepository = new SegnalazioneRepository();
        HackathonRepository hackathonRepository = new HackathonRepository();
        UtenteRepository utenteRepository = new UtenteRepository();
        TeamRepository teamRepository = new TeamRepository();
        InvitoRepository invitoRepository = new InvitoRepository();

        TeamService teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        HackathonService hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        SegnalazioneService segnalazioneService = new SegnalazioneService(
                segnalazioneRepository, hackathonRepository, utenteRepository, teamRepository);
        segnalazioneHandler = new SegnalazioneHandler(segnalazioneService);

        Utente organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
    }

    @Test
    void testSegnalaSuccess() {
        Result<String> result = segnalazioneHandler.segnala(
                1L, "Comportamento scorretto", hackathon.getId());

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertEquals("Segnalazione inviata con successo", result.getData());
    }

    @Test
    void testSegnalaHackathonNonTrovato() {
        Result<String> result = segnalazioneHandler.segnala(
                1L, "Descrizione", 9999L);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
