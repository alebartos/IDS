package it.unicam.ids.service;

import it.unicam.ids.builder.HackathonBuilder;
import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.UtenteRepository;

import java.time.LocalDate;

/**
 * Service per la gestione degli Hackathon.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final UtenteRepository utenteRepository;

    public HackathonService(HackathonRepository hackathonRepository, UtenteRepository utenteRepository) {
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
    }

    public Hackathon creaHackathon(Utente organizzatore, HackathonRequest request) {
        if (organizzatore == null) {
            throw new IllegalArgumentException("L'organizzatore è obbligatorio");
        }

        if (!organizzatore.hasRuolo(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("L'utente deve avere il ruolo ORGANIZZATORE");
        }

        if (esisteHackathonConNome(request.getNome())) {
            throw new IllegalArgumentException("Esiste già un hackathon con questo nome");
        }

        if (!validaDate(request.getDataInizio(), request.getDataFine(), request.getScadenzaIscrizioni())) {
            throw new IllegalArgumentException("Le date non sono valide");
        }

        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome(request.getNome())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .scadenzaIscrizioni(request.getScadenzaIscrizioni())
                .luogo(request.getLuogo())
                .regolamento(request.getRegolamento())
                .premio(request.getPremio())
                .maxMembriTeam(request.getMaxMembriTeam() != null ? request.getMaxMembriTeam() : 5)
                .build();

        hackathon.setOrganizzatoreId(organizzatore.getId());

        return hackathonRepository.save(hackathon);
    }

    public boolean esisteHackathonConNome(String nome) {
        return hackathonRepository.existsByNome(nome);
    }

    public boolean validaDate(LocalDate dataInizio, LocalDate dataFine, LocalDate scadenzaIscrizioni) {
        if (dataInizio == null || dataFine == null || scadenzaIscrizioni == null) {
            return false;
        }

        if (scadenzaIscrizioni.isAfter(dataInizio)) {
            return false;
        }

        if (dataInizio.isAfter(dataFine) || dataInizio.isEqual(dataFine)) {
            return false;
        }

        return true;
    }

    public Hackathon getDettagliHackathon(Long hackathonId) {
        return hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }

    public int getMaxMembriTeam(Long hackathonId) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        return hackathon.getMaxMembriTeam();
    }

    public boolean checkValiditaHackathon(Long hackathonId) {
        try {
            Hackathon hackathon = getDettagliHackathon(hackathonId);
            LocalDate oggi = LocalDate.now();

            if (oggi.isBefore(hackathon.getScadenzaIscrizioni())) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Assegna un giudice a un hackathon. Solo un organizzatore può assegnare giudici.
     * @param hackathonId ID dell'hackathon
     * @param giudiceId ID dell'utente da assegnare come giudice
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere ORGANIZZATORE)
     * @return l'hackathon aggiornato
     */
    public Hackathon assegnaGiudice(Long hackathonId, Long giudiceId, Long richiedenteId) {
        // Verifica che il richiedente sia un organizzatore
        Utente richiedente = utenteRepository.findById(richiedenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente richiedente non trovato"));

        if (!richiedente.hasRuolo(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può assegnare giudici");
        }

        if (giudiceId == null) {
            throw new IllegalArgumentException("L'ID del giudice è obbligatorio");
        }

        Hackathon hackathon = getDettagliHackathon(hackathonId);

        if (hackathon.hasGiudice()) {
            throw new IllegalArgumentException("L'hackathon ha già un giudice assegnato");
        }

        Utente giudice = utenteRepository.findById(giudiceId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        giudice.addRuolo(Ruolo.GIUDICE);
        utenteRepository.save(giudice);

        hackathon.setGiudiceId(giudiceId);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Rimuove il giudice da un hackathon. Solo un organizzatore può rimuovere giudici.
     * @param hackathonId ID dell'hackathon
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere ORGANIZZATORE)
     * @return l'hackathon aggiornato
     */
    public Hackathon rimuoviGiudice(Long hackathonId, Long richiedenteId) {
        // Verifica che il richiedente sia un organizzatore
        Utente richiedente = utenteRepository.findById(richiedenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente richiedente non trovato"));

        if (!richiedente.hasRuolo(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può rimuovere giudici");
        }

        Hackathon hackathon = getDettagliHackathon(hackathonId);

        if (!hackathon.hasGiudice()) {
            throw new IllegalArgumentException("L'hackathon non ha un giudice assegnato");
        }

        Long giudiceId = hackathon.getGiudiceId();
        if (giudiceId != null) {
            Utente giudice = utenteRepository.findById(giudiceId).orElse(null);
            if (giudice != null) {
                giudice.deleteRuolo(Ruolo.GIUDICE);
                utenteRepository.save(giudice);
            }
        }

        hackathon.setGiudiceId(null);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Aggiunge un membro allo staff dell'hackathon. Solo un organizzatore può aggiungere staff.
     * @param hackathonId ID dell'hackathon
     * @param utenteId ID dell'utente da aggiungere allo staff
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere ORGANIZZATORE)
     * @return l'hackathon aggiornato
     */
    public Hackathon aggiungiMembroStaff(Long hackathonId, Long utenteId, Long richiedenteId) {
        // Verifica che il richiedente sia un organizzatore
        Utente richiedente = utenteRepository.findById(richiedenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente richiedente non trovato"));

        if (!richiedente.hasRuolo(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può aggiungere membri allo staff");
        }

        Hackathon hackathon = getDettagliHackathon(hackathonId);

        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (hackathon.checkStaff(utenteId)) {
            throw new IllegalArgumentException("L'utente è già membro dello staff");
        }

        utente.addRuolo(Ruolo.MEMBRO_STAFF);
        utenteRepository.save(utente);

        hackathon.addMembroStaffId(utenteId);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Rimuove un membro dallo staff dell'hackathon. Solo un organizzatore può rimuovere staff.
     * @param hackathonId ID dell'hackathon
     * @param utenteId ID dell'utente da rimuovere dallo staff
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere ORGANIZZATORE)
     * @return l'hackathon aggiornato
     */
    public Hackathon rimuoviMembroStaff(Long hackathonId, Long utenteId, Long richiedenteId) {
        // Verifica che il richiedente sia un organizzatore
        Utente richiedente = utenteRepository.findById(richiedenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente richiedente non trovato"));

        if (!richiedente.hasRuolo(Ruolo.ORGANIZZATORE)) {
            throw new IllegalArgumentException("Solo un organizzatore può rimuovere membri dallo staff");
        }

        Hackathon hackathon = getDettagliHackathon(hackathonId);

        if (!hackathon.checkStaff(utenteId)) {
            throw new IllegalArgumentException("L'utente non è membro dello staff");
        }

        hackathon.removeMembroStaffId(utenteId);
        return hackathonRepository.save(hackathon);
    }

    /**
     * Iscrive un team a un hackathon. Solo il leader del team può iscriverlo.
     * @param hackathon l'hackathon
     * @param team il team
     * @param richiedenteId ID dell'utente che sta effettuando l'iscrizione (deve essere il leader del team)
     */
    public void iscriviTeam(Hackathon hackathon, Team team, Long richiedenteId) {
        if (hackathon == null || team == null) {
            throw new IllegalArgumentException("Hackathon e Team sono obbligatori");
        }

        // Verifica che il richiedente sia il leader del team
        if (team.getLeaderId() == null || !team.getLeaderId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può iscriverlo a un hackathon");
        }

        if (!checkValiditaHackathon(hackathon.getId())) {
            throw new IllegalArgumentException("Le iscrizioni per questo hackathon sono chiuse");
        }
    }
}
