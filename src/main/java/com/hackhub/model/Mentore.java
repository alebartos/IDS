package com.hackhub.model;

import com.hackhub.enums.StatoCall;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un Mentore in HackHub.
 * <p>
 * Il Mentore supporta i team durante gli hackathon attraverso:
 * - Proposte di call di mentoring
 * - Prenotazione di slot per le call
 * <p>
 * Estende: MembroStaff
 */
public class Mentore extends MembroStaff {

    /** Area di specializzazione del mentore */
    private String specializzazione;

    /** Lista delle call proposte dal mentore */
    private List<Call> callProposte;

    /** Lista degli hackathon a cui il mentore è associato */
    private List<Hackathon> hackathonAssociati;

    /**
     * Costruttore della classe Mentore.
     *
     * @param nome     Il nome del mentore
     * @param cognome  Il cognome del mentore
     * @param email    L'email del mentore
     * @param password La password del mentore
     */
    public Mentore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.setRuolo("Mentore");
        this.callProposte = new ArrayList<>();
        this.hackathonAssociati = new ArrayList<>();
    }

    // ==================== GETTER ====================

    /**
     * Restituisce la specializzazione del mentore.
     *
     * @return La specializzazione
     */
    public String getSpecializzazione() {
        return specializzazione;
    }

    /**
     * Restituisce la lista delle call proposte.
     *
     * @return Lista delle call
     */
    public List<Call> getCallProposte() {
        return callProposte;
    }

    /**
     * Restituisce la lista degli hackathon associati.
     *
     * @return Lista degli hackathon
     */
    public List<Hackathon> getHackathonAssociati() {
        return hackathonAssociati;
    }

    // ==================== SETTER ====================

    /**
     * Imposta la specializzazione del mentore.
     *
     * @param specializzazione La specializzazione da impostare
     */
    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Propone una call di mentoring a un team.
     * <p>
     * Precondizioni:
     * - Il team deve essere iscritto a un hackathon associato al mentore
     * <p>
     * Postcondizioni:
     * - Viene creata una nuova Call con stato PROPOSTA
     * - La call viene aggiunta alla lista delle call del mentore
     *
     * @param team Il team a cui proporre la call
     * @return La call creata
     */
    public Call proponiCall(Team team) {
        // Crea la nuova call
        Call nuovaCall = new Call(this, team);

        // Aggiunge alla lista delle call proposte
        this.callProposte.add(nuovaCall);

        return nuovaCall;
    }

    /**
     * Prenota uno slot per una call confermata.
     * <p>
     * Precondizioni:
     * - La call deve essere in stato CONFERMATA
     * - Il mentore deve essere il proprietario della call
     * <p>
     * Postcondizioni:
     * - La call passa allo stato PRENOTATA
     * - Vengono impostati data e ora della call
     *
     * @param call    La call da prenotare
     * @param dataOra Data e ora dello slot
     * @throws IllegalStateException se la call non è in stato CONFERMATA
     * @throws IllegalArgumentException se il mentore non è il proprietario della call
     */
    public void prenotaSlot(Call call, LocalDateTime dataOra) {
        // Verifica che la call appartenga al mentore
        if (!call.getMentore().equals(this)) {
            throw new IllegalArgumentException("Non sei il mentore di questa call");
        }

        // Verifica stato call
        if (call.getStato() != StatoCall.CONFERMATA) {
            throw new IllegalStateException("La call non è in stato CONFERMATA");
        }

        // Prenota lo slot
        call.setDataOra(dataOra);
        call.setStato(StatoCall.PRENOTATA);
    }

    /**
     * Aggiunge un hackathon alla lista di quelli associati al mentore.
     *
     * @param hackathon L'hackathon da associare
     */
    public void aggiungiHackathon(Hackathon hackathon) {
        this.hackathonAssociati.add(hackathon);
    }

    /**
     * Restituisce le call in attesa di prenotazione (stato CONFERMATA).
     *
     * @return Lista delle call confermate da prenotare
     */
    public List<Call> getCallDaPrenotare() {
        List<Call> daPrenotare = new ArrayList<>();
        for (Call call : callProposte) {
            if (call.getStato() == StatoCall.CONFERMATA) {
                daPrenotare.add(call);
            }
        }
        return daPrenotare;
    }
}
