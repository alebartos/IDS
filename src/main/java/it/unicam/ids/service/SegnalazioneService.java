package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.UtenteRepository;

public class SegnalazioneService {

    private final SegnalazioneRepository segnalazioneRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;

    public SegnalazioneService(SegnalazioneRepository segnalazioneRepo, HackathonRepository hackathonRepo,
                               UtenteRepository utenteRepo) {
        this.segnalazioneRepo = segnalazioneRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
    }

    public void segnala(Long teamId, String descrizione, Long hackathonId) {
        Segnalazione segnalazione = new Segnalazione();
        segnalazione.setDescrizione(descrizione);
        segnalazione.setTeamId(teamId);
        segnalazione.setHackathonId(hackathonId);
        segnalazioneRepo.add(segnalazione);

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        Utente organizzatore = utenteRepo.findById(hackathon.getOrganizzatoreId())
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));

        inviaMail(organizzatore.getEmail(), "Nuova segnalazione");
    }

    public void inviaMail(String email, String oggetto) {
        System.out.println("[EMAIL] Invio mail a: " + email + " - Oggetto: " + oggetto);
    }
}
