package it.unicam.ids.controller;

import it.unicam.ids.model.Team;
import it.unicam.ids.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamHandler {

    private final TeamService teamService;

    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<?> creaTeam(@RequestBody Map<String, Object> body) {
        try {
            String nomeTeam = (String) body.get("nome");
            Long leaderId = ((Number) body.get("leaderId")).longValue();
            Team team = teamService.createTeam(nomeTeam, leaderId);
            return ResponseEntity.status(201).body(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{leaderId}/membri/{membroId}")
    public ResponseEntity<?> rimuoviMembro(@PathVariable Long leaderId, @PathVariable Long membroId) {
        try {
            Team team = teamService.rimuoviMembro(membroId, leaderId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{leaderId}/viceleader/{membroId}")
    public ResponseEntity<?> nominaViceleader(@PathVariable Long leaderId, @PathVariable Long membroId) {
        try {
            Team team = teamService.nominaViceleader(leaderId, membroId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{leaderId}/viceleader/{membroId}")
    public ResponseEntity<?> revocaViceleader(@PathVariable Long leaderId, @PathVariable Long membroId) {
        try {
            Team team = teamService.removeViceleader(leaderId, membroId);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
