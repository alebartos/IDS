package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Invito;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.InvitoService;
import it.unicam.ids.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InvitoHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private InvitoService invitoService;

    @Autowired
    private TeamService teamService;

    private Utente destinatario;
    private Team team;

    @BeforeEach
    void setUp() {
        Utente leader = new Utente("Mario", "Rossi", "mario.rossi@example.com", "password123");
        leader = utenteRepository.save(leader);
        team = teamService.createTeam("Team Invito", leader.getId());

        destinatario = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password456");
        destinatario = utenteRepository.save(destinatario);
    }

    @Test
    void testInvitaMembroSuccess() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>(Map.of("email", "anna.bianchi@example.com", "teamId", team.getId()));

        mockMvc.perform(post("/api/inviti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invito inviato con successo"));
    }

    @Test
    void testInvitaMembroUtenteNonTrovato() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>(Map.of("email", "inesistente@example.com", "teamId", team.getId()));

        mockMvc.perform(post("/api/inviti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvitaMembroDuplicato() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>(Map.of("email", "anna.bianchi@example.com", "teamId", team.getId()));

        mockMvc.perform(post("/api/inviti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/inviti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGestisciInvitoAccettato() throws Exception {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId());

        Map<String, String> body = Map.of("risposta", "ACCETTATO");

        mockMvc.perform(put("/api/inviti/{id}", invito.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invito gestito con successo"));
    }

    @Test
    void testGestisciInvitoRifiutato() throws Exception {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId());

        Map<String, String> body = Map.of("risposta", "RIFIUTATO");

        mockMvc.perform(put("/api/inviti/{id}", invito.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invito gestito con successo"));
    }

    @Test
    void testGestisciInvitoRispostaNonValida() throws Exception {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId());

        Map<String, String> body = Map.of("risposta", "INVALIDA");

        mockMvc.perform(put("/api/inviti/{id}", invito.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGestisciInvitoGiaGestito() throws Exception {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com", team.getId());

        Map<String, String> body = Map.of("risposta", "ACCETTATO");

        mockMvc.perform(put("/api/inviti/{id}", invito.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/inviti/{id}", invito.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
