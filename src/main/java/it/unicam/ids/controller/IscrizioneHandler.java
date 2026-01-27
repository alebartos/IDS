package it.unicam.ids.controller;

import it.unicam.ids.model.*;
import it.unicam.ids.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iscrizioni")
public class IscrizioneHandler {

    @Autowired
    private HackathonService hackathonService;

    @Autowired
    private TeamService teamService;

    @PostMapping("/iscriviTeam")
    public ResponseEntity<?> iscriviTeam(@RequestParam Long hackathonId,
                                          @RequestParam Long teamId) {
        try {
            Hackathon hackathon = hackathonService.getDettagliHackathon(hackathonId);
            Team team = teamService.getDettagliTeam(teamId);

            hackathonService.iscriviTeam(hackathon, team);

            return ResponseEntity.ok("Team iscritto con successo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
