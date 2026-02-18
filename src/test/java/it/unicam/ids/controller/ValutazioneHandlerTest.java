package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.SottomissioneService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ValutazioneHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private SottomissioneService sottomissioneService;

    @Autowired
    private SottomissioneRepository sottomissioneRepository;

    private Sottomissione sottomissione;
    private Utente giudice;

    @BeforeEach
    void setUp() {
        Utente organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        Utente leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        Team team = teamService.createTeam("Team Alpha", leader.getId());

        Hackathon hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Desc",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1), 5, 1000.0, organizzatore.getId());

        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathon = hackathonRepository.save(hackathon);

        leader.addRuolo(Ruolo.PARTECIPANTE);
        leader = utenteRepository.save(leader);

        sottomissione = sottomissioneService.gestisciBozze(team.getId(), hackathon.getId(), leader.getId());

        sottomissione.setStato(StatoSottomissione.CONSEGNATA);
        sottomissione = sottomissioneRepository.save(sottomissione);

        giudice = new Utente("Paolo", "Neri", "paolo.neri@example.com", "password789");
        giudice.addRuolo(Ruolo.GIUDICE);
        giudice = utenteRepository.save(giudice);
    }

    @Test
    void testConfermaSottomissioneSuccess() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("giudiceId", giudice.getId());
        body.put("sottomissioneId", sottomissione.getId());
        body.put("punteggio", 85);
        body.put("giudizio", "Ottimo lavoro");

        mockMvc.perform(post("/api/valutazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sottomissione.getId()))
                .andExpect(jsonPath("$.stato").value("VALUTATA"));
    }

    @Test
    void testConfermaSottomissioneGiaValutata() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("giudiceId", giudice.getId());
        body.put("sottomissioneId", sottomissione.getId());
        body.put("punteggio", 85);
        body.put("giudizio", "Ottimo lavoro");

        mockMvc.perform(post("/api/valutazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/valutazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfermaSottomissioneNonConsegnata() throws Exception {
        Utente altroLeader = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password000");
        altroLeader = utenteRepository.save(altroLeader);

        Team altroTeam = teamService.createTeam("Team Beta", altroLeader.getId());

        Hackathon hackathon = hackathonRepository.findByNome("Hackathon Test").orElseThrow();

        altroLeader.addRuolo(Ruolo.PARTECIPANTE);
        altroLeader = utenteRepository.save(altroLeader);

        Sottomissione bozza = sottomissioneService.gestisciBozze(altroTeam.getId(), hackathon.getId(), altroLeader.getId());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("giudiceId", giudice.getId());
        body.put("sottomissioneId", bozza.getId());
        body.put("punteggio", 70);
        body.put("giudizio", "Buon lavoro");

        mockMvc.perform(post("/api/valutazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfermaSottomissioneGiudizioVuoto() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("giudiceId", giudice.getId());
        body.put("sottomissioneId", sottomissione.getId());
        body.put("punteggio", 85);
        body.put("giudizio", "");

        mockMvc.perform(post("/api/valutazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
