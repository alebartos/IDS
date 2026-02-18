package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.model.Utente;
import it.unicam.ids.builder.SottomissioneBuilder;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class SottomissioneService {

    private final SottomissioneRepository sottomissioneRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;

    public SottomissioneService(SottomissioneRepository sottomissioneRepo,
                                HackathonRepository hackathonRepo, UtenteRepository utenteRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
    }

    public void checkRuoloPartecipante(Long utenteId) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        if (!utente.getRuoli().contains(Ruolo.PARTECIPANTE)
                && !utente.getRuoli().contains(Ruolo.MEMBRO_TEAM)
                && !utente.getRuoli().contains(Ruolo.LEADER)) {
            throw new IllegalArgumentException("Non sei un partecipante");
        }
    }

    public void checkHackathonAttivo(Long hackathonId) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (hackathon.getStato() != StatoHackathon.IN_CORSO) {
            throw new IllegalArgumentException("Hackathon non attivo");
        }
    }

    public void checkTeamIscritto(Long teamId, Long hackathonId) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (!hackathon.getTeamIds().contains(teamId)) {
            throw new IllegalArgumentException("Team non iscritto a questo hackathon");
        }
    }

    public Sottomissione gestisciBozze(Long teamId, Long hackathonId) {
        checkHackathonAttivo(hackathonId);
        Sottomissione sottomissione = sottomissioneRepo.findByTeamIdAndHackathonId(teamId, hackathonId).orElse(null);
        if (sottomissione != null) {
            if (sottomissione.getStato() == StatoSottomissione.CONSEGNATA) {
                throw new IllegalArgumentException("Sottomissione definitiva già inviata, non è possibile modificarla");
            }
            return sottomissione;
        }
        sottomissione = SottomissioneBuilder.newBuilder()
                .teamId(teamId).hackathonId(hackathonId).stato(StatoSottomissione.BOZZA).build();
        return sottomissioneRepo.save(sottomissione);
    }

    public Sottomissione elaboraSottomissione(Long teamId, Long hackathonId, DatiProgetto datiProgetto) {
        checkHackathonAttivo(hackathonId);
        checkTeamIscritto(teamId, hackathonId);
        Sottomissione sottomissione = sottomissioneRepo.findByTeamIdAndHackathonId(teamId, hackathonId).orElse(null);
        if (sottomissione != null) {
            if (sottomissione.getStato() == StatoSottomissione.CONSEGNATA) {
                throw new IllegalArgumentException("Sottomissione definitiva già inviata");
            }
            sottomissione.setDatiProgetto(datiProgetto);
            sottomissione.setStato(StatoSottomissione.CONSEGNATA);
            sottomissione.setDataInvio(LocalDate.now());
            sottomissioneRepo.save(sottomissione);
            return sottomissione;
        }
        sottomissione = SottomissioneBuilder.newBuilder()
                .teamId(teamId).hackathonId(hackathonId).datiProgetto(datiProgetto).stato(StatoSottomissione.CONSEGNATA).build();
        sottomissione.setDataInvio(LocalDate.now());
        return sottomissioneRepo.save(sottomissione);
    }

    public void checkValiditàLink(String linkRepository) {
        if (linkRepository == null || linkRepository.isEmpty()) {
            throw new IllegalArgumentException("Il link alla repository è obbligatorio");
        }
        if (!linkRepository.startsWith("http://") && !linkRepository.startsWith("https://")) {
            throw new IllegalArgumentException("Il link alla repository non è valido");
        }
    }

    public DatiProgetto creaDTO(String titolo, String descrizione, String linkRepository) {
        return new DatiProgetto(titolo, descrizione, linkRepository);
    }
}
