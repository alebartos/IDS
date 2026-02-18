package it.unicam.ids.controller;

import it.unicam.ids.service.IscrizioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/iscrizioni")
public class IscrizioneHandler {

    private final IscrizioneService iscrizioneService;

    public IscrizioneHandler(IscrizioneService iscrizioneService) {
        this.iscrizioneService = iscrizioneService;
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/team/{teamId}/hackathon/{hackathonId}")
    public ResponseEntity<?> iscriviTeam(@PathVariable Long teamId, @PathVariable Long hackathonId) {
        try {
            Map<String, Object> dati = iscrizioneService.selezionaPartecipanti(teamId, hackathonId);
            List<Long> membri = (List<Long>) dati.get("membri");
            iscrizioneService.iscriviTeam(teamId, hackathonId, membri);
            return ResponseEntity.ok(Map.of("message", "Team iscritto con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/team/{teamId}/hackathon/{hackathonId}/partecipanti")
    public ResponseEntity<?> selezionaPartecipanti(@PathVariable Long teamId, @PathVariable Long hackathonId) {
        try {
            Map<String, Object> dati = iscrizioneService.selezionaPartecipanti(teamId, hackathonId);
            return ResponseEntity.ok(dati);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/team/{teamId}/hackathon/{hackathonId}/partecipanti")
    public ResponseEntity<?> selezionaPartecipantiConSelezione(@PathVariable Long teamId, @PathVariable Long hackathonId, @RequestBody Map<String, Object> body) {
        try {
            List<Number> selectedRaw = (List<Number>) body.get("partecipanti");
            List<Long> selected = selectedRaw.stream().map(Number::longValue).toList();
            iscrizioneService.iscriviTeam(teamId, hackathonId, selected);
            return ResponseEntity.ok(Map.of("message", "Partecipanti selezionati e team iscritto con successo"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
