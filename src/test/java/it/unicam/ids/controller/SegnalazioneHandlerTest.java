package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.SegnalazioneService;
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
class SegnalazioneHandlerTest {

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
    private SegnalazioneService segnalazioneService;

    @Autowired
    private SegnalazioneRepository segnalazioneRepository;

    private Utente organizzatore;
    private Utente leader;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.save(leader);

        team = teamService.createTeam("Team Alpha", leader.getId());

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Desc",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());

        hackathon.getTeamIds().add(team.getId());
        hackathon = hackathonRepository.save(hackathon);
    }

    @Test
    void testSegnalaSuccess() throws Exception {
        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "descrizione", "Comportamento scorretto",
                "hackathonId", hackathon.getId()
        );

        mockMvc.perform(post("/api/segnalazioni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Segnalazione inviata con successo"));
    }

    @Test
    void testGetSegnalazioniEmpty() throws Exception {
        mockMvc.perform(get("/api/segnalazioni/hackathon/{hackathonId}", hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetSegnalazioniNonEmpty() throws Exception {
        segnalazioneService.segnala(team.getId(), "Descrizione test", hackathon.getId());

        mockMvc.perform(get("/api/segnalazioni/hackathon/{hackathonId}", hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testArchiviaSegnalazioneSuccess() throws Exception {
        segnalazioneService.segnala(team.getId(), "Da archiviare", hackathon.getId());
        Long segnalazioneId = segnalazioneRepository.findByHackathonId(hackathon.getId()).get(0).getId();

        mockMvc.perform(put("/api/segnalazioni/{id}/archivia", segnalazioneId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Segnalazione archiviata con successo"));
    }

    @Test
    void testArchiviaSegnalazioneNonTrovata() throws Exception {
        mockMvc.perform(put("/api/segnalazioni/{id}/archivia", 9999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testSqualificaTeamSuccess() throws Exception {
        segnalazioneService.segnala(team.getId(), "Da squalificare", hackathon.getId());
        Long segnalazioneId = segnalazioneRepository.findByHackathonId(hackathon.getId()).get(0).getId();

        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId()
        );

        mockMvc.perform(put("/api/segnalazioni/{id}/squalifica", segnalazioneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Team squalificato con successo"));
    }

    @Test
    void testSqualificaTeamNonPartecipante() throws Exception {
        segnalazioneService.segnala(team.getId(), "Team non presente", hackathon.getId());
        Long segnalazioneId = segnalazioneRepository.findByHackathonId(hackathon.getId()).get(0).getId();

        // Rimuovi il team dall'hackathon
        hackathon.getTeamIds().remove(team.getId());
        hackathonRepository.save(hackathon);

        Map<String, Object> body = Map.of(
                "teamId", team.getId(),
                "hackathonId", hackathon.getId()
        );

        mockMvc.perform(put("/api/segnalazioni/{id}/squalifica", segnalazioneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
