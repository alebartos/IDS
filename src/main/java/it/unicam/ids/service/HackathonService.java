package it.unicam.ids.service;

import it.unicam.ids.builder.HackathonBuilder;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.UtenteRepository;

import java.time.LocalDate;

/**
 * Service per la gestione degli Hackathon.
 */
public class HackathonService {

    private final HackathonRepository hackathonRepo;
    private final UtenteRepository utenteRepo;
    private final TeamService teamService;

    public HackathonService(HackathonRepository hackathonRepo, UtenteRepository utenteRepo, TeamService teamService) {
        this.hackathonRepo = hackathonRepo;
        this.utenteRepo = utenteRepo;
        this.teamService = teamService;
    }

    /**
     * Crea un nuovo hackathon.
     * @param nome il nome dell'hackathon
     * @param descrizione la descrizione
     * @param dataInizio la data di inizio
     * @param dataFine la data di fine
     * @param maxPartecipanti il numero massimo di partecipanti per team
     * @param premio il premio in denaro
     * @param organizzatoreId l'ID dell'organizzatore
     * @return l'hackathon creato
     */
    public Hackathon createHackathon(String nome, String descrizione, LocalDate dataInizio,
                                     LocalDate dataFine, int maxPartecipanti, double premio,
                                     Long organizzatoreId) {
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
                .nome(nome)
                .descrizione(descrizione)
                .dataInizio(dataInizio)
                .dataFine(dataFine)
                .premio(premio)
                .maxMembriTeam(maxPartecipanti)
                .build();

        hackathon.setOrganizzatoreId(organizzatoreId);

        return hackathonRepo.save(hackathon);
    }

    /**
     * Assegna un giudice a un hackathon tramite email.
     * @param hackathonId ID dell'hackathon
     * @param email email dell'utente da assegnare come giudice
     * @param organizzatoreId ID dell'organizzatore che effettua l'operazione
     */
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

    /**
     * Verifica se esiste un hackathon con il nome specificato.
     * @param nome il nome da verificare
     * @return true se esiste, false altrimenti
     */
    private boolean esisteHackathonConNome(String nome) {
        return hackathonRepo.existsByNome(nome);
    }

    /**
     * Valida le date dell'hackathon.
     * @param dataInizio la data di inizio
     * @param dataFine la data di fine
     * @return true se le date sono valide, false altrimenti
     */
    private boolean validaDate(LocalDate dataInizio, LocalDate dataFine) {
        if (dataInizio == null || dataFine == null) {
            return false;
        }

        if (dataInizio.isAfter(dataFine) || dataInizio.isEqual(dataFine)) {
            return false;
        }

        return true;
    }

    /**
     * Assegna un mentore a un hackathon tramite email.
     * @param email email dell'utente da assegnare come mentore
     * @param organizzatoreId ID dell'organizzatore che effettua l'operazione
     */
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

    /**
     * Proclama il team vincitore di un hackathon.
     * @param hackathonId ID dell'hackathon
     * @param teamId ID del team vincitore
     */
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

    /**
     * Cambia lo stato di un hackathon.
     * @param hackathonId ID dell'hackathon
     * @param nuovoStato il nuovo stato da impostare
     */
    public void modifcaStato(Long hackathonId, StatoHackathon nuovoStato) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));

        StatoHackathon statoCorrente = hackathon.getStato();

        switch (nuovoStato) {
            case IN_CORSO:
                if (statoCorrente != StatoHackathon.IN_ISCRIZIONE) {
                    throw new IllegalArgumentException("L'hackathon può passare a IN_CORSO solo da IN_ISCRIZIONE");
                }
                if (hackathon.getDataInizio().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("La data di inizio non è ancora stata raggiunta");
                }
                break;
            case IN_VALUTAZIONE:
                if (statoCorrente != StatoHackathon.IN_CORSO) {
                    throw new IllegalArgumentException("L'hackathon può passare a IN_VALUTAZIONE solo da IN_CORSO");
                }
                if (hackathon.getDataFine().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("La data di fine non è ancora stata raggiunta");
                }
                break;
            case CONCLUSO:
                if (statoCorrente != StatoHackathon.IN_VALUTAZIONE) {
                    throw new IllegalArgumentException("L'hackathon può passare a CONCLUSO solo da IN_VALUTAZIONE");
                }
                if (hackathon.getTeamVincitoreId() == null) {
                    throw new IllegalArgumentException("Devi proclamare un vincitore prima di concludere l'hackathon");
                }
                break;
            case ANNULLATO:
                if (statoCorrente == StatoHackathon.CONCLUSO) {
                    throw new IllegalArgumentException("Un hackathon concluso non può essere annullato");
                }
                break;
            default:
                throw new IllegalArgumentException("Transizione di stato non valida");
        }

        hackathon.setStato(nuovoStato);
        hackathonRepo.save(hackathon);
    }

    /**
     * Verifica la validità di un ruolo.
     * @param ruolo il ruolo da verificare
     * @return true se il ruolo è valido (non null), false altrimenti
     */
    public boolean checkRuolo(Ruolo ruolo) {
        return ruolo != null;
    }

    /**
     * Verifica se un utente esiste nel sistema.
     * @param utenteId l'ID dell'utente da verificare
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean checkId(Long utenteId) {
        if (utenteId == null) {
            return false;
        }
        return utenteRepo.existsById(utenteId);
    }

    /**
     * Verifica se un team è iscritto a qualche hackathon.
     * @param teamId l'ID del team da verificare
     * @return true se il team è iscritto ad almeno un hackathon, false altrimenti
     */
    public boolean findByTeamId(Long teamId) {
        return hackathonRepo.findAll().stream()
                .anyMatch(h -> h.getTeamIds().contains(teamId));
    }
}
