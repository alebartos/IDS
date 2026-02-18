package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.service.SottomissioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sottomissioni")
public class SottomissioneHandler {

    private final SottomissioneService sottomissioneService;

    public SottomissioneHandler(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    @PostMapping
    public ResponseEntity<?> caricaSottomissione(@RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            String titolo = (String) body.get("titolo");
            String descrizione = (String) body.get("descrizione");
            String linkRepository = (String) body.get("linkRepository");
            boolean isDefinitiva = body.containsKey("definitiva") && (boolean) body.get("definitiva");

            DatiProgetto datiProgetto = new DatiProgetto(titolo, descrizione, linkRepository);

            if (isDefinitiva) {
                sottomissioneService.checkValiditàLink(linkRepository);
            }

            Sottomissione sottomissione = sottomissioneService.gestisciBozze(teamId, hackathonId);
            sottomissione.setDatiProgetto(datiProgetto);
            if (isDefinitiva) {
                sottomissione.setStato(StatoSottomissione.CONSEGNATA);
            }
            return ResponseEntity.ok(sottomissione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
