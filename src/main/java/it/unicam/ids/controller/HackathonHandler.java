package it.unicam.ids.controller;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.service.HackathonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hackathon")
public class HackathonHandler {

    private final HackathonService hackathonService;

    public HackathonHandler(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    @PostMapping
    public ResponseEntity<?> creaHackathon(@RequestBody Map<String, Object> body) {
        try {
            String nome = (String) body.get("nome");
            String descrizione = (String) body.get("descrizione");
            LocalDate dataInizio = LocalDate.parse((String) body.get("dataInizio"));
            LocalDate dataFine = LocalDate.parse((String) body.get("dataFine"));
            LocalDate scadenzaIscrizioni = LocalDate.parse((String) body.get("scadenzaIscrizioni"));
            int maxPartecipanti = ((Number) body.get("maxPartecipanti")).intValue();
            double premio = ((Number) body.get("premio")).doubleValue();
            Long organizzatoreId = ((Number) body.get("organizzatoreId")).longValue();
            Hackathon hackathon = hackathonService.createHackathon(nome, descrizione, dataInizio, dataFine, scadenzaIscrizioni, maxPartecipanti, premio, organizzatoreId);
            return ResponseEntity.status(201).body(hackathon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getHackathons() {
        try {
            List<Hackathon> hackathons = hackathonService.getHackathons();
            return ResponseEntity.ok(hackathons);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<?> getTeams(@PathVariable Long id) {
        try {
            List<Team> teams = hackathonService.getTeams(id);
            return ResponseEntity.ok(teams);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/giudice")
    public ResponseEntity<?> assegnaGiudice(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            String email = (String) body.get("email");
            Long organizzatoreId = ((Number) body.get("organizzatoreId")).longValue();
            hackathonService.assegnaGiudice(id, email, organizzatoreId);
            return ResponseEntity.ok(Map.of("message", "Giudice assegnato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/mentore")
    public ResponseEntity<?> assegnaMentore(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            String email = (String) body.get("email");
            Long organizzatoreId = ((Number) body.get("organizzatoreId")).longValue();
            hackathonService.assegnaMentore(email, organizzatoreId);
            return ResponseEntity.ok(Map.of("message", "Mentore assegnato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/stato")
    public ResponseEntity<?> cambiaStato(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            StatoHackathon nuovoStato = StatoHackathon.valueOf(body.get("stato"));
            hackathonService.modifcaStato(id, nuovoStato);
            return ResponseEntity.ok(Map.of("message", "Stato aggiornato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/vincitore")
    public ResponseEntity<?> proclamaVincitore(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            hackathonService.proclamaVincitore(id, teamId);
            return ResponseEntity.ok(Map.of("message", "Vincitore proclamato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
