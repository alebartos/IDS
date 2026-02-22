package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.model.Utente;
import it.unicam.ids.builder.SottomissioneBuilder;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SottomissioneService {

    private final SottomissioneRepository sottomissioneRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;

    public SottomissioneService(SottomissioneRepository sottomissioneRepo,
                                HackathonRepository hackathonRepo, UtenteRepository utenteRepo,
                                TeamRepository teamRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
    }

    public boolean checkRuolo(Ruolo ruolo) {
        return ruolo == Ruolo.PARTECIPANTE || ruolo == Ruolo.MEMBRO_TEAM || ruolo == Ruolo.LEADER;
    }

    public Sottomissione gestisciBozze(Long teamId, Long hackathonId, Long utenteId) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        boolean hasRole = utente.getRuoli().contains(Ruolo.PARTECIPANTE)
                || utente.getRuoli().contains(Ruolo.MEMBRO_TEAM)
                || utente.getRuoli().contains(Ruolo.LEADER);
        if (!hasRole) {
            throw new IllegalArgumentException("Non sei un partecipante");
        }
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (hackathon.getStato() != StatoHackathon.IN_CORSO) {
            throw new IllegalArgumentException("Hackathon non attivo");
        }
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

    public void checkLeader(Long utenteId, Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        if (!team.getLeaderId().equals(utenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può inviare la sottomissione finale");
        }
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

    public Sottomissione save(Sottomissione sottomissione) {
        return sottomissioneRepo.save(sottomissione);
    }

    public List<Sottomissione> getValutazioni(Long hackathonId) {
        return sottomissioneRepo.findByHackathonId(hackathonId);
    }
}
