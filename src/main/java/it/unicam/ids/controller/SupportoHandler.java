package it.unicam.ids.controller;

import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.service.SupportoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supporto")
public class SupportoHandler {

    private final SupportoService supportoService;

    public SupportoHandler(SupportoService supportoService) {
        this.supportoService = supportoService;
    }

    @PostMapping("/richieste")
    public ResponseEntity<?> inviaRichiestaSupporto(@RequestBody Map<String, Object> body) {
        try {
            String descrizione = (String) body.get("descrizione");
            Long utenteId = ((Number) body.get("utenteId")).longValue();
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            supportoService.elaboraRichiestaSupporto(descrizione, utenteId, hackathonId);
            return ResponseEntity.ok(Map.of("message", "Richiesta di supporto inviata con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/richieste/hackathon/{hackathonId}")
    public ResponseEntity<?> getRichieste(@PathVariable Long hackathonId) {
        try {
            List<RichiestaSupporto> richieste = supportoService.creaListaRichieste(hackathonId);
            return ResponseEntity.ok(richieste);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/call")
    public ResponseEntity<?> prenotaCall(@RequestBody Map<String, Object> body) {
        try {
            Long richiestaId = ((Number) body.get("richiestaId")).longValue();
            LocalDate data = LocalDate.parse((String) body.get("data"));
            LocalTime oraInizio = LocalTime.parse((String) body.get("oraInizio"));
            LocalTime oraFine = LocalTime.parse((String) body.get("oraFine"));
            supportoService.prenotaCall(richiestaId, data, oraInizio, oraFine);
            return ResponseEntity.ok(Map.of("message", "Call prenotata con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/call/{eventId}/conferma")
    public ResponseEntity<?> confermaPartecipazione(@PathVariable String eventId) {
        try {
            supportoService.confermaPartecipazione(eventId);
            return ResponseEntity.ok(Map.of("message", "Partecipazione confermata con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
