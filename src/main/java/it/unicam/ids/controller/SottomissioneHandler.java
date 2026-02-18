package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.service.SottomissioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sottomissioni")
public class SottomissioneHandler {

    private final SottomissioneService sottomissioneService;

    public SottomissioneHandler(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    @PostMapping("/bozza")
    public ResponseEntity<?> caricaBozza(@RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            Long utenteId = ((Number) body.get("utenteId")).longValue();
            String titolo = (String) body.get("titolo");
            String descrizione = (String) body.get("descrizione");
            String linkRepository = (String) body.get("linkRepository");

            DatiProgetto datiProgetto = new DatiProgetto(titolo, descrizione, linkRepository);
            Sottomissione sottomissione = sottomissioneService.gestisciBozze(teamId, hackathonId, utenteId);
            sottomissione.setDatiProgetto(datiProgetto);
            return ResponseEntity.ok(sottomissione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sottometti")
    public ResponseEntity<?> sottomettiProgetto(@RequestBody Map<String, Object> body) {
        try {
            Long teamId = ((Number) body.get("teamId")).longValue();
            Long hackathonId = ((Number) body.get("hackathonId")).longValue();
            Long utenteId = ((Number) body.get("utenteId")).longValue();
            String titolo = (String) body.get("titolo");
            String descrizione = (String) body.get("descrizione");
            String linkRepository = (String) body.get("linkRepository");

            sottomissioneService.checkValiditàLink(linkRepository);
            DatiProgetto datiProgetto = new DatiProgetto(titolo, descrizione, linkRepository);
            Sottomissione sottomissione = sottomissioneService.gestisciBozze(teamId, hackathonId, utenteId);
            sottomissione.setDatiProgetto(datiProgetto);
            sottomissione.setStato(StatoSottomissione.CONSEGNATA);
            return ResponseEntity.ok(sottomissione);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/valutazioni/{hackathonId}")
    public ResponseEntity<?> getValutazioni(@PathVariable Long hackathonId) {
        try {
            List<Sottomissione> valutazioni = sottomissioneService.getValutazioni(hackathonId);
            return ResponseEntity.ok(valutazioni);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
