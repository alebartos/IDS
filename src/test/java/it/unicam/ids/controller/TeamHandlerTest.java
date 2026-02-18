package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeamHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    private Utente leader;
    private Utente membro;

    @BeforeEach
    void setUp() {
        leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);

        membro = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password456");
        membro = utenteRepository.save(membro);
    }

    @Test
    void testCreaTeamSuccess() throws Exception {
        Map<String, Object> body = Map.of("nome", "Team Alpha", "leaderId", leader.getId());

        mockMvc.perform(post("/api/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Team Alpha"))
                .andExpect(jsonPath("$.leaderId").value(leader.getId()));
    }

    @Test
    void testCreaTeamNomeDuplicato() throws Exception {
        teamService.createTeam("Team Alpha", leader.getId());

        Utente altroLeader = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password789");
        altroLeader = utenteRepository.save(altroLeader);

        Map<String, Object> body = Map.of("nome", "Team Alpha", "leaderId", altroLeader.getId());

        mockMvc.perform(post("/api/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreaTeamUtenteNonTrovato() throws Exception {
        Map<String, Object> body = Map.of("nome", "Team Fantasma", "leaderId", 9999L);

        mockMvc.perform(post("/api/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNominaViceleaderSuccess() throws Exception {
        Team team = teamService.createTeam("Team Vice", leader.getId());
        team.getMembri().add(membro.getId());
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        utenteRepository.save(membro);
        teamRepository.save(team);

        mockMvc.perform(put("/api/team/{leaderId}/viceleader/{membroId}",
                        leader.getId(), membro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viceleaderId").value(membro.getId()));
    }

    @Test
    void testNominaViceleaderNonMembro() throws Exception {
        teamService.createTeam("Team Vice NM", leader.getId());

        mockMvc.perform(put("/api/team/{leaderId}/viceleader/{membroId}",
                        leader.getId(), membro.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRevocaViceleaderSuccess() throws Exception {
        Team team = teamService.createTeam("Team Revoca", leader.getId());
        team.getMembri().add(membro.getId());
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        utenteRepository.save(membro);
        teamRepository.save(team);

        teamService.nominaViceleader(leader.getId(), membro.getId());

        mockMvc.perform(delete("/api/team/{leaderId}/viceleader/{membroId}",
                        leader.getId(), membro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viceleaderId").isEmpty());
    }

    @Test
    void testRimuoviMembroSuccess() throws Exception {
        Team team = teamService.createTeam("Team Rimuovi", leader.getId());
        team.getMembri().add(membro.getId());
        membro.getRuoli().add(Ruolo.MEMBRO_TEAM);
        utenteRepository.save(membro);
        teamRepository.save(team);

        mockMvc.perform(delete("/api/team/{leaderId}/membri/{membroId}",
                        leader.getId(), membro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.membri").isArray());
    }

    @Test
    void testRimuoviMembroSelf() throws Exception {
        teamService.createTeam("Team Self", leader.getId());

        mockMvc.perform(delete("/api/team/{leaderId}/membri/{membroId}",
                        leader.getId(), leader.getId()))
                .andExpect(status().isBadRequest());
    }
}
