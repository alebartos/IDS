package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.service.ValutazioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/valutazioni")
public class ValutazioneHandler {

    private final ValutazioneService valutazioneService;

    public ValutazioneHandler(ValutazioneService valutazioneService) {
        this.valutazioneService = valutazioneService;
    }

    @PostMapping
    public ResponseEntity<?> confermaSottomissione(@RequestBody Map<String, Object> body) {
        try {
            Long giudiceId = ((Number) body.get("giudiceId")).longValue();
            Long sottomissioneId = ((Number) body.get("sottomissioneId")).longValue();
            int punteggio = ((Number) body.get("punteggio")).intValue();
            String giudizio = (String) body.get("giudizio");
            DatiValutazione datiValutazione = valutazioneService.creaDTO(punteggio, giudizio);
            Sottomissione sottomissione = valutazioneService.valutaSottomissione(giudiceId, sottomissioneId, datiValutazione);
            return ResponseEntity.ok(sottomissione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
