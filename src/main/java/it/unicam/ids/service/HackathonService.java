package it.unicam.ids.service;

import it.unicam.ids.builder.HackathonBuilder;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class HackathonService {

    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;

    public HackathonService(HackathonRepository hackathonRepo, UtenteRepository utenteRepo,
                             TeamRepository teamRepo) {
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
    }

    public Hackathon createHackathon(String nome, String descrizione, LocalDate dataInizio,
                                      LocalDate dataFine, LocalDate scadenzaIscrizioni,
                                      int maxPartecipanti, double premio, Long organizzatoreId) {
        checkId(organizzatoreId);
        Utente organizzatore = utenteRepo.findById(organizzatoreId)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        if (!organizzatore.getRuoli().contains(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo ORGANIZZATORE");
        }
        if (esisteHackathonConNome(nome)) {
            throw new IllegalArgumentException("Esiste già un hackathon con questo nome");
        }
        if (!validaDate(dataInizio, dataFine)) {
            throw new IllegalArgumentException("Le date non sono valide");
        }
        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome(nome).descrizione(descrizione).dataInizio(dataInizio).dataFine(dataFine)
                .scadenzaIscrizioni(scadenzaIscrizioni).premio(String.valueOf(premio)).maxMembriTeam(maxPartecipanti).build();
        hackathon.setOrganizzatoreId(organizzatoreId);
        return hackathonRepo.save(hackathon);
    }

    public void assegnaGiudice(Long hackathonId, String email, Long organizzatoreId) {
        Utente organizzatore = utenteRepo.findById(organizzatoreId)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        if (!organizzatore.getRuoli().contains(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può assegnare giudici");
        }
        Utente giudice = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (hackathon.getGiudiceId() != null) {
            throw new IllegalArgumentException("L'hackathon ha già un giudice assegnato");
        }
        if (!giudice.getRuoli().contains(Ruolo.MEMBRO_STAFF)) {
            throw new IllegalArgumentException("L'utente non fa parte dello staff");
        }
        giudice.getRuoli().add(Ruolo.GIUDICE);
        utenteRepo.save(giudice);
        hackathon.setGiudiceId(giudice.getId());
        hackathonRepo.save(hackathon);
    }

    private boolean esisteHackathonConNome(String nome) {
        return hackathonRepo.findByNome(nome).isPresent();
    }

    private boolean validaDate(LocalDate dataInizio, LocalDate dataFine) {
        if (dataInizio == null || dataFine == null) return false;
        if (dataInizio.isAfter(dataFine) || dataInizio.isEqual(dataFine)) return false;
        return true;
    }

    public void assegnaMentore(String email, Long organizzatoreId) {
        Utente organizzatore = utenteRepo.findById(organizzatoreId)
                .orElseThrow(() -> new IllegalArgumentException("Organizzatore non trovato"));
        if (!organizzatore.getRuoli().contains(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può assegnare mentori");
        }
        Utente utente = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        Hackathon hackathon = hackathonRepo.findByOrganizzatoreId(organizzatoreId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato per questo organizzatore"));
        if (hackathon.getMentoreIds().contains(utente.getId())) {
            throw new IllegalArgumentException("L'utente è già mentore di questo hackathon");
        }
        if (utente.getRuoli().contains(Ruolo.ORGANIZZATORE) || utente.getRuoli().contains(Ruolo.GIUDICE)) {
            throw new IllegalArgumentException("L'utente non può essere mentore per incompatibilità di ruoli");
        }
        utente.getRuoli().add(Ruolo.MENTORE);
        utenteRepo.save(utente);
        hackathon.getMentoreIds().add(utente.getId());
        hackathonRepo.save(hackathon);
    }

    public void proclamaVincitore(Long hackathonId, Long teamId) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        if (hackathon.getStato() != StatoHackathon.IN_VALUTAZIONE) {
            throw new IllegalArgumentException("L'hackathon deve essere in stato IN_VALUTAZIONE per proclamare un vincitore");
        }
        if (!hackathon.getTeamIds().contains(teamId)) {
            throw new IllegalArgumentException("Il team selezionato non partecipa all'hackathon");
        }
        hackathon.setTeamVincitoreId(teamId);
        hackathon.setStato(StatoHackathon.CONCLUSO);
        hackathonRepo.save(hackathon);
    }

    public void modifcaStato(Long hackathonId, StatoHackathon nuovoStato) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        StatoHackathon statoCorrente = hackathon.getStato();
        switch (nuovoStato) {
            case IN_CORSO:
                if (statoCorrente != StatoHackathon.IN_ISCRIZIONE)
                    throw new IllegalArgumentException("L'hackathon può passare a IN_CORSO solo da IN_ISCRIZIONE");
                if (hackathon.getDataInizio().isAfter(LocalDate.now()))
                    throw new IllegalArgumentException("La data di inizio non è ancora stata raggiunta");
                break;
            case IN_VALUTAZIONE:
                if (statoCorrente != StatoHackathon.IN_CORSO)
                    throw new IllegalArgumentException("L'hackathon può passare a IN_VALUTAZIONE solo da IN_CORSO");
                if (hackathon.getDataFine().isAfter(LocalDate.now()))
                    throw new IllegalArgumentException("La data di fine non è ancora stata raggiunta");
                break;
            case CONCLUSO:
                if (statoCorrente != StatoHackathon.IN_VALUTAZIONE)
                    throw new IllegalArgumentException("L'hackathon può passare a CONCLUSO solo da IN_VALUTAZIONE");
                if (hackathon.getTeamVincitoreId() == null)
                    throw new IllegalArgumentException("Devi proclamare un vincitore prima di concludere l'hackathon");
                break;
            case ANNULLATO:
                if (statoCorrente == StatoHackathon.CONCLUSO)
                    throw new IllegalArgumentException("Un hackathon concluso non può essere annullato");
                removeRuolo();
                break;
            default:
                throw new IllegalArgumentException("Transizione di stato non valida");
        }
        hackathon.setStato(nuovoStato);
        hackathonRepo.save(hackathon);
    }

    public boolean checkRuolo(Ruolo ruolo) { return ruolo != null; }

    public boolean checkId(Long utenteId) {
        if (utenteId == null) return false;
        return utenteRepo.existsById(utenteId);
    }

    public boolean findByTeamId(Long teamId) {
        return hackathonRepo.findAll().stream().anyMatch(h -> h.getTeamIds().contains(teamId));
    }

    public List<Hackathon> creaListaHackathon() {
        return new ArrayList<>(hackathonRepo.findAll());
    }

    public List<Hackathon> getHackathons() { return creaListaHackathon(); }

    public List<Team> getTeams(Long hackathonId) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        List<Team> teams = new ArrayList<>();
        for (Long teamId : hackathon.getTeamIds()) {
            teamRepo.findById(teamId).ifPresent(teams::add);
        }
        return teams;
    }

    public List<Team> creaListaTeam() {
        return new ArrayList<>(teamRepo.findAll());
    }

    public void removeRuolo() {
        List<Utente> partecipanti = utenteRepo.findByRuoliContaining(Ruolo.PARTECIPANTE);
        for (Utente u : partecipanti) {
            u.getRuoli().remove(Ruolo.PARTECIPANTE);
            utenteRepo.save(u);
        }
    }
}
