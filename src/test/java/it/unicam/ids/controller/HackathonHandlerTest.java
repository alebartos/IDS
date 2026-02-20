package it.unicam.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.UtenteRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HackathonHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    private Utente organizzatore;

    @BeforeEach
    void setUp() {
        organizzatore = new Utente("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
        organizzatore.addRuolo(Ruolo.ORGANIZZATORE);
        organizzatore.addRuolo(Ruolo.MEMBRO_STAFF);
        organizzatore = utenteRepository.save(organizzatore);
    }

    private Map<String, Object> buildHackathonBody(String nome, String descrizione,
                                                   String dataInizio, String dataFine,
                                                   String scadenzaIscrizioni,
                                                   int maxPartecipanti, double premio,
                                                   Long organizzatoreId) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("nome", nome);
        body.put("descrizione", descrizione);
        body.put("dataInizio", dataInizio);
        body.put("dataFine", dataFine);
        body.put("scadenzaIscrizioni", scadenzaIscrizioni);
        body.put("maxPartecipanti", maxPartecipanti);
        body.put("premio", premio);
        body.put("organizzatoreId", organizzatoreId);
        return body;
    }

    private Hackathon createHackathonViaService(String nome, String dataInizio, String dataFine) throws Exception {
        Map<String, Object> body = buildHackathonBody(
                nome, "Descrizione test",
                dataInizio, dataFine,
                "2025-02-15", 5, 1000.0, organizzatore.getId());

        String response = mockMvc.perform(post("/api/hackathon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, Hackathon.class);
    }

    @Test
    void testCreaHackathonSuccess() throws Exception {
        Map<String, Object> body = buildHackathonBody(
                "Hackathon 2025", "Test event",
                "2025-03-01", "2025-03-03",
                "2025-02-15", 5, 1000.0, organizzatore.getId());

        mockMvc.perform(post("/api/hackathon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Hackathon 2025"))
                .andExpect(jsonPath("$.organizzatoreId").value(organizzatore.getId()));
    }

    @Test
    void testCreaHackathonOrganizzatoreNonTrovato() throws Exception {
        Map<String, Object> body = buildHackathonBody(
                "Hackathon Fail", "Test event",
                "2025-03-01", "2025-03-03",
                "2025-02-15", 5, 1000.0, 99999L);

        mockMvc.perform(post("/api/hackathon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testGetHackathonsEmpty() throws Exception {
        mockMvc.perform(get("/api/hackathon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetHackathonsNonEmpty() throws Exception {
        createHackathonViaService("Hackathon List Test", "2025-03-01", "2025-03-03");

        mockMvc.perform(get("/api/hackathon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Hackathon List Test"));
    }

    @Test
    void testGetTeams() throws Exception {
        Hackathon hackathon = createHackathonViaService("Hackathon Teams Test", "2025-03-01", "2025-03-03");

        mockMvc.perform(get("/api/hackathon/" + hackathon.getId() + "/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testAssegnaGiudiceSuccess() throws Exception {
        Hackathon hackathon = createHackathonViaService("Hackathon Giudice", "2025-03-01", "2025-03-03");

        Utente giudice = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        giudice.addRuolo(Ruolo.MEMBRO_STAFF);
        giudice = utenteRepository.save(giudice);

        Hackathon h = hackathonRepository.findById(hackathon.getId()).orElseThrow();
        h.getMembroStaffIds().add(giudice.getId());
        hackathonRepository.save(h);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("email", "paolo@example.com");
        body.put("organizzatoreId", organizzatore.getId());

        mockMvc.perform(put("/api/hackathon/" + hackathon.getId() + "/giudice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Giudice assegnato con successo"));
    }

    @Test
    void testAssegnaGiudiceNonStaff() throws Exception {
        Hackathon hackathon = createHackathonViaService("Hackathon Giudice Fail", "2025-03-01", "2025-03-03");

        Utente utenteNormale = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        utenteRepository.save(utenteNormale);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("email", "anna@example.com");
        body.put("organizzatoreId", organizzatore.getId());

        mockMvc.perform(put("/api/hackathon/" + hackathon.getId() + "/giudice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testAssegnaMentoreSuccess() throws Exception {
        createHackathonViaService("Hackathon Mentore", "2025-03-01", "2025-03-03");

        Utente mentore = new Utente("Marco", "Neri", "marco@example.com", "password");
        mentore = utenteRepository.save(mentore);

        Hackathon hackathon = hackathonRepository.findByNome("Hackathon Mentore").orElseThrow();
        hackathon.getMembroStaffIds().add(mentore.getId());
        hackathonRepository.save(hackathon);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("email", "marco@example.com");
        body.put("organizzatoreId", organizzatore.getId());

        mockMvc.perform(put("/api/hackathon/" + hackathon.getId() + "/mentore")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mentore assegnato con successo"));
    }

    @Test
    void testCambiaStatoSuccess() throws Exception {
        Map<String, Object> createBody = buildHackathonBody(
                "Hackathon Stato", "Descrizione",
                LocalDate.now().minusDays(1).toString(),
                LocalDate.now().plusDays(5).toString(),
                LocalDate.now().minusDays(2).toString(),
                5, 1000.0, organizzatore.getId());

        String response = mockMvc.perform(post("/api/hackathon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBody)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Hackathon hackathon = objectMapper.readValue(response, Hackathon.class);

        Map<String, String> statoBody = new LinkedHashMap<>();
        statoBody.put("stato", "IN_CORSO");

        mockMvc.perform(put("/api/hackathon/" + hackathon.getId() + "/stato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statoBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Stato aggiornato con successo"));
    }

    @Test
    void testCambiaStatoInvalido() throws Exception {
        Hackathon hackathon = createHackathonViaService("Hackathon Stato Invalido", "2025-03-01", "2025-03-03");

        Map<String, String> statoBody = new LinkedHashMap<>();
        statoBody.put("stato", "CONCLUSO");

        mockMvc.perform(put("/api/hackathon/" + hackathon.getId() + "/stato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statoBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
