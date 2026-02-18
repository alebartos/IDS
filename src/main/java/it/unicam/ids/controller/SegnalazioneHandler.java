package it.unicam.ids.controller;

import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.service.SegnalazioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/segnalazioni")
public class SegnalazioneHandler {

    private final SegnalazioneService segnalazioneService;

    public SegnalazioneHandler(SegnalazioneService segnalazioneService) {
        this.segnalazioneService = segnalazioneService;
    }

    @PostMapping
    public ResponseEntity<?> segnala(@RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            String descrizione = (String) body.get("descrizione");
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            segnalazioneService.segnala(teamId, descrizione, hackathonId);
            return ResponseEntity.ok(Map.of("message", "Segnalazione inviata con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<?> getSegnalazioni(@PathVariable Long hackathonId) {
        try {
            List<Segnalazione> segnalazioni = segnalazioneService.getSegnalazioni(hackathonId);
            return ResponseEntity.ok(segnalazioni);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/archivia")
    public ResponseEntity<?> gestisciSegnalazione(@PathVariable Long id) {
        try {
            segnalazioneService.archiviaSegnalazione(id);
            return ResponseEntity.ok(Map.of("message", "Segnalazione archiviata con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/squalifica")
    public ResponseEntity<?> gestisciSegnalazione(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            segnalazioneService.squalificaTeam(id, teamId, hackathonId);
            return ResponseEntity.ok(Map.of("message", "Team squalificato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
