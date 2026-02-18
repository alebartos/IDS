package it.unicam.ids.service;

import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SupportoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SupportoService {

    private final SupportoRepository supportoRepo;
    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;
    private final TeamRepository teamRepo;
    private final CalendarService calendarService;
    private final ObserverSupporto observerSupporto;

    public SupportoService(SupportoRepository supportoRepo, HackathonRepository hackathonRepo,
                           UtenteRepository utenteRepo, TeamRepository teamRepo,
                           CalendarService calendarService, ObserverSupporto observerSupporto) {
        this.supportoRepo = supportoRepo;
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
        this.teamRepo = teamRepo;
        this.calendarService = calendarService;
        this.observerSupporto = observerSupporto;
    }

    public boolean checkRuolo(Long utenteId) {
        Utente utente = utenteRepo.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        return utente.getRuoli().contains(Ruolo.PARTECIPANTE)
                || utente.getRuoli().contains(Ruolo.MEMBRO_TEAM)
                || utente.getRuoli().contains(Ruolo.LEADER)
                || utente.getRuoli().contains(Ruolo.VICELEADER);
    }

    public boolean checkStato(Long hackathonId) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        return hackathon.getStato() == StatoHackathon.IN_CORSO;
    }

    public List<RichiestaSupporto> creaListaRichieste(Long hackathonId) {
        List<RichiestaSupporto> richieste = supportoRepo.getAllRichieste(hackathonId);
        List<RichiestaSupporto> lista = new ArrayList<>();
        for (RichiestaSupporto r : richieste) {
            lista.add(r);
        }
        return lista;
    }

    public void elaboraRichiestaSupporto(String descrizione, Long utenteId, Long hackathonId) {
        if (!checkRuolo(utenteId)) {
            throw new IllegalArgumentException("L'utente non è un partecipante");
        }

        if (!checkStato(hackathonId)) {
            throw new IllegalArgumentException("L'hackathon non è attivo");
        }

        Team team = teamRepo.findByUtenteId(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato per l'utente"));

        RichiestaSupporto richiesta = new RichiestaSupporto(descrizione, team.getId(), hackathonId);
        supportoRepo.add(richiesta);

        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        for (Long mentoreId : hackathon.getMentoreIds()) {
            Utente mentore = utenteRepo.findById(mentoreId).orElse(null);
            if (mentore != null) {
                observerSupporto.notify("Il mentore " + mentoreId + " ha una nuova richiesta di supporto");
            }
        }
    }

    public void prenotaCall(Long richiestaId, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        checkDate(data, oraInizio, oraFine);

        RichiestaSupporto richiesta = supportoRepo.findById(richiestaId)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata"));

        Team team = teamRepo.findById(richiesta.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

        List<String> listaMail = creaListaMail(team);

        calendarService.prenotaCall(richiesta.getDescrizione(), data, oraInizio, oraFine, listaMail);

        richiesta.setRisolta(true);
        supportoRepo.modifyRecord(richiesta);
    }

    public void checkDate(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        if (data == null || oraInizio == null || oraFine == null) {
            throw new IllegalArgumentException("La data e gli orari non possono essere nulli");
        }
        if (oraInizio.isAfter(oraFine)) {
            throw new IllegalArgumentException("L'orario di inizio non può essere dopo l'orario di fine");
        }
    }

    public void confermaPartecipazione(String eventId) {
        calendarService.confermaPartecipazione(eventId);
    }

    public List<String> creaListaMail(Team team) {
        List<String> listaMail = new ArrayList<>();
        for (Long membroId : team.getMembri()) {
            Utente utente = utenteRepo.findById(membroId).orElse(null);
            if (utente != null) {
                listaMail.add(utente.getEmail());
            }
        }
        Utente leader = utenteRepo.findById(team.getLeaderId()).orElse(null);
        if (leader != null) {
            listaMail.add(leader.getEmail());
        }
        return listaMail;
    }
}
