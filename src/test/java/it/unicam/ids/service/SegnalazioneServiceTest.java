package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SegnalazioneServiceTest {

    @Autowired
    private SegnalazioneService segnalazioneService;

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private SegnalazioneRepository segnalazioneRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    private Utente organizzatore;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Description",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
    }

    @Test
    void testSegnalaSuccess() {
        assertDoesNotThrow(() ->
                segnalazioneService.segnala(1L, "Comportamento scorretto", hackathon.getId()));

        java.util.List<Segnalazione> segnalazioni = segnalazioneRepository.findByHackathonId(hackathon.getId());
        assertEquals(1, segnalazioni.size());
        Segnalazione segnalazione = segnalazioni.get(0);
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
