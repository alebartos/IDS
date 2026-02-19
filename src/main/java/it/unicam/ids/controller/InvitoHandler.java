package it.unicam.ids.controller;

import it.unicam.ids.service.InvitoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inviti")
public class InvitoHandler {

    private final InvitoService invitoService;

    public InvitoHandler(InvitoService invitoService) {
        this.invitoService = invitoService;
    }

    @PostMapping
    public ResponseEntity<?> invitaMembro(@RequestBody Map<String, Object> body) {
        try {
            String email = (String) body.get("email");
            Long teamId = ((Number) body.get("teamId")).longValue();
            invitoService.invitaMembro(email, teamId);
            return ResponseEntity.ok(Map.of("message", "Invito inviato con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> gestisciInvito(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String risposta = body.get("risposta");
            invitoService.gestisciInvito(id, risposta);
            return ResponseEntity.ok(Map.of("message", "Invito gestito con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
