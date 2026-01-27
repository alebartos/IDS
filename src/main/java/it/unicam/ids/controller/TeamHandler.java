package it.unicam.ids.controller;

import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Team;
import it.unicam.ids.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamHandler {

    @Autowired
    private TeamService teamService;

    @PostMapping("/crea")
    public ResponseEntity<?> creaTeam(@RequestBody TeamRequest request) {
        try {
            Team team = teamService.creaTeam(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errore", "Dati non validi per la creazione del team"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDettagliTeam(@PathVariable Long id) {
        try {
            Team team = teamService.getDettagliTeam(id);
            return ResponseEntity.ok(team);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errore", "Team non trovato"));
        }
    }

    @GetMapping("/{id}/membri")
    public ResponseEntity<?> getMembriTeam(@PathVariable Long id) {
        try {
            List<Object> membri = teamService.getMembriTeam(id);
            return ResponseEntity.ok(membri);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errore", "Team non trovato"));
        }
    }

    @GetMapping("/esiste/{nome}")
    public ResponseEntity<Boolean> esisteTeam(@PathVariable String nome) {
        boolean existe = teamService.esisteTeamConNome(nome);
        return ResponseEntity.ok(existe);
    }
}