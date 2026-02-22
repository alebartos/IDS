package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SupportoRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.SupportoService;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SupportoHandlerTest {

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
    private SupportoService supportoService;

    @Autowired
    private SupportoRepository supportoRepository;

    private Utente organizzatore;
    private Utente leader;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        // Crea organizzatore
        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore.addRuolo(Ruolo.MEMBRO_STAFF);
        organizzatore = utenteRepository.save(organizzatore);

        // Crea leader e team
        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.save(leader);
        team = teamService.createTeam("Team Alpha", leader.getId());

        // Crea hackathon e imposta stato IN_CORSO
        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Desc",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());
        hackathon.setStato(StatoHackathon.IN_CORSO);
        hackathon = hackathonRepository.save(hackathon);

        // Aggiungi ruolo PARTECIPANTE al leader
        leader.addRuolo(Ruolo.PARTECIPANTE);
        leader = utenteRepository.save(leader);
    }

    @Test
    void testInviaRichiestaSupportoSuccess() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("descrizione", "Problema tecnico");
        body.put("utenteId", leader.getId());
        body.put("hackathonId", hackathon.getId());

        mockMvc.perform(post("/api/supporto/richieste")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Richiesta di supporto inviata con successo"));
    }

    @Test
    void testInviaRichiestaSupportoUtenteNonPartecipante() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("descrizione", "Problema tecnico");
        body.put("utenteId", organizzatore.getId());
        body.put("hackathonId", hackathon.getId());

        mockMvc.perform(post("/api/supporto/richieste")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testGetRichiesteEmpty() throws Exception {
        mockMvc.perform(get("/api/supporto/richieste/hackathon/{hackathonId}", hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetRichiesteNonEmpty() throws Exception {
        supportoService.elaboraRichiestaSupporto("Problema tecnico", leader.getId(), hackathon.getId());

        mockMvc.perform(get("/api/supporto/richieste/hackathon/{hackathonId}", hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testPrenotaCallSuccess() throws Exception {
        supportoService.elaboraRichiestaSupporto("Problema tecnico", leader.getId(), hackathon.getId());
        List<RichiestaSupporto> richieste = supportoService.creaListaRichieste(hackathon.getId());
        RichiestaSupporto richiesta = richieste.get(0);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("richiestaId", richiesta.getId());
        body.put("data", LocalDate.now().plusDays(1).toString());
        body.put("oraInizio", "10:00");
        body.put("oraFine", "11:00");

        mockMvc.perform(post("/api/supporto/call")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Call prenotata con successo"));
    }

}
