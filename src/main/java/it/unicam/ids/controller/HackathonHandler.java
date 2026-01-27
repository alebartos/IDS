package it.unicam.ids.controller;

import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.OrganizzatoreRepository;
import it.unicam.ids.service.HackathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hackathon")
public class HackathonHandler {

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private OrganizzatoreRepository organizzatoreRepository;

    @PostMapping("/crea")
    public ResponseEntity<?> creaHackathon(@RequestBody HackathonRequest request,
                                           @RequestParam Long organizzatoreId) {
        try {
            Organizzatore organizzatore = organizzatoreRepository.findById(organizzatoreId)
                    .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));

            Hackathon hackathon = hackathonService.creaHackathon(organizzatore, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(hackathon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("errore", "Dati non validi per la creazione dell'hackathon"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDettagliHackathon(@PathVariable Long id) {
        try {
            Hackathon hackathon = hackathonService.getDettagliHackathon(id);
            return ResponseEntity.ok(hackathon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errore", "Hackathon non trovato"));
        }
    }

    @GetMapping("/{id}/max-membri")
    public ResponseEntity<?> getMaxMembriTeam(@PathVariable Long id) {
        try {
            int maxMembri = hackathonService.getMaxMembriTeam(id);
            return ResponseEntity.ok(maxMembri);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errore", "Hackathon non trovato"));
        }
    }

    @GetMapping("/{id}/validita")
    public ResponseEntity<Boolean> checkValidita(@PathVariable Long id) {
        boolean valido = hackathonService.checkValiditaHackathon(id);
        return ResponseEntity.ok(valido);
    }

    @GetMapping("/esiste/{nome}")
    public ResponseEntity<Boolean> esisteHackathon(@PathVariable String nome) {
        boolean existe = hackathonService.esisteHackathonConNome(nome);
        return ResponseEntity.ok(existe);
    }
}