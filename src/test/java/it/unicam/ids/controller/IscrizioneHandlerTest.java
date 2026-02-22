package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.TeamRepository;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IscrizioneHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private TeamService teamService;

    private Utente organizzatore;
    private Utente leader;
    private Utente membro;
    private Team team;
    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        organizzatore = new Utente("Luigi", "Verdi", "luigi@example.com", "password");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore.getRuoli().add(Ruolo.MEMBRO_STAFF);
        organizzatore = utenteRepository.save(organizzatore);

        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader = utenteRepository.save(leader);

        team = teamService.createTeam("Team Test", leader.getId());

        hackathon = hackathonService.createHackathon(
                "Hackathon Test", "Descrizione test",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(1),
                5, 1000.0, organizzatore.getId());

        membro = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        membro = utenteRepository.save(membro);
        team.getMembri().add(membro.getId());
        teamRepository.save(team);
    }

    @Test
    void testIscriviTeamSuccess() throws Exception {
        mockMvc.perform(post("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}",
                        team.getId(), hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Team iscritto con successo"));
    }

    @Test
    void testIscriviTeamGiaIscritto() throws Exception {
        // Prima iscrizione
        mockMvc.perform(post("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}",
                        team.getId(), hackathon.getId()))
                .andExpect(status().isOk());

        // Seconda iscrizione: deve fallire
        mockMvc.perform(post("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}",
                        team.getId(), hackathon.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testSelezionaPartecipantiSuccess() throws Exception {
        mockMvc.perform(get("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}/partecipanti",
                        team.getId(), hackathon.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.membri").isArray())
                .andExpect(jsonPath("$.maxMembriTeam").isNumber());
    }

    @Test
    void testSelezionaPartecipantiConSelezioneSuccess() throws Exception {
        List<Long> partecipanti = List.of(membro.getId());
        Map<String, Object> body = Map.of("partecipanti", partecipanti);

        mockMvc.perform(post("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}/partecipanti",
                        team.getId(), hackathon.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Partecipanti selezionati e team iscritto con successo"));
    }

    @Test
    void testIscriviTeamHackathonNonTrovato() throws Exception {
        mockMvc.perform(post("/api/iscrizioni/team/{teamId}/hackathon/{hackathonId}",
                        team.getId(), 99999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
