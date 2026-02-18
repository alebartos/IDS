package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
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
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SottomissioneHandlerTest {

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

    private Utente leader;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        Utente organizzatore = new Utente("Admin", "Org", "admin.org@example.com", "password123");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        team = teamService.createTeam("Team Alpha", leader.getId());

        hackathon = hackathonService.createHackathon(
                "Hackathon Test",
                "Description",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5,
                1000.0,
                organizzatore.getId()
        );

        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathon = hackathonRepository.save(hackathon);

        leader.addRuolo(Ruolo.PARTECIPANTE);
        leader = utenteRepository.save(leader);
    }

    @Test
    void testCaricaBozzaSuccess() throws Exception {
        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId(),
                "utenteId", leader.getId(),
                "titolo", "Progetto Test",
                "descrizione", "Descrizione del progetto",
                "linkRepository", "https://github.com/test/repo"
        );

        mockMvc.perform(post("/api/sottomissioni/bozza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testSottomettiProgettoSuccess() throws Exception {
        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId(),
                "utenteId", leader.getId(),
                "titolo", "Progetto Finale",
                "descrizione", "Descrizione finale",
                "linkRepository", "https://github.com/test/repo"
        );

        mockMvc.perform(post("/api/sottomissioni/sottometti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testSottomettiProgettoLinkInvalido() throws Exception {
        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId(),
                "utenteId", leader.getId(),
                "titolo", "Progetto Invalido",
                "descrizione", "Descrizione",
                "linkRepository", "ftp://invalid-link.com/repo"
        );

        mockMvc.perform(post("/api/sottomissioni/sottometti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetValutazioniEmpty() throws Exception {
        mockMvc.perform(get("/api/sottomissioni/valutazioni/{hackathonId}", hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCaricaBozzaUtenteNonPartecipante() throws Exception {
        Utente nonPartecipante = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        nonPartecipante = utenteRepository.save(nonPartecipante);

        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId(),
                "utenteId", nonPartecipante.getId(),
                "titolo", "Progetto Non Autorizzato",
                "descrizione", "Descrizione",
                "linkRepository", "https://github.com/test/repo"
        );

        mockMvc.perform(post("/api/sottomissioni/bozza")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
