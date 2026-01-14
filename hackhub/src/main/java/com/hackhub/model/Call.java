package com.hackhub.model;

import com.hackhub.state.IStatoCall;
import com.hackhub.state.StatoCallProposta;
import java.time.LocalDateTime;

/**
 * Classe che rappresenta una Call di mentoring in HackHub.
 *
 * Una Call e' una sessione di mentoring tra un Mentore e un Team.
 * Ciclo di vita:
 * PROPOSTA -> CONFERMATA -> PRENOTATA -> COMPLETATA
 * (Puo' essere ANNULLATA in qualsiasi momento)
 *
 * Relazioni:
 * - Composizione con Mentore (la call non esiste senza mentore)
 * - Aggregazione con Team
 *
 * Design Pattern: State (per la gestione degli stati)
 */
public class Call {

    /** Identificativo univoco della call */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Stato corrente della call (State Pattern) */
    private IStatoCall statoCall;

    /** Data e ora della call (impostata dopo la prenotazione) */
    private LocalDateTime dataOra;

    /** Durata della call in minuti */
    private int durata;

    /** Note aggiuntive */
    private String note;

    /** ID della prenotazione nel sistema Calendar (esterno) */
    private String idPrenotazione;

    /** Mentore che ha proposto la call */
    private Mentore mentore;

    /** Team coinvolto nella call */
    private Team team;

    /**
     * Costruttore della classe Call.
     *
     * @param mentore Il mentore che propone la call
     * @param team    Il team destinatario
     */
    public Call(Mentore mentore, Team team) {
        this.id = contatoreId++;
        this.mentore = mentore;
        this.team = team;
        this.statoCall = new StatoCallProposta();
        this.durata = 30; // Default 30 minuti
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID della call.
     *
     * @return L'ID della call
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce lo stato della call.
     *
     * @return Lo stato corrente
     */
    public IStatoCall getStatoCall() {
        return statoCall;
    }

    /**
     * Restituisce il nome dello stato corrente.
     *
     * @return Il nome dello stato
     */
    public String getStatoNome() {
        return statoCall.getNomeStato();
    }

    /**
     * Restituisce la data e ora della call.
     *
     * @return La data e ora, o null se non ancora prenotata
     */
    public LocalDateTime getDataOra() {
        return dataOra;
    }

    /**
     * Restituisce la durata della call in minuti.
     *
     * @return La durata in minuti
     */
    public int getDurata() {
        return durata;
    }

    /**
     * Restituisce le note della call.
     *
     * @return Le note
     */
    public String getNote() {
        return note;
    }

    /**
     * Restituisce l'ID della prenotazione nel sistema Calendar.
     *
     * @return L'ID prenotazione, o null se non prenotata
     */
    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    /**
     * Restituisce il mentore che ha proposto la call.
     *
     * @return Il mentore
     */
    public Mentore getMentore() {
        return mentore;
    }

    /**
     * Restituisce il team coinvolto.
     *
     * @return Il team
     */
    public Team getTeam() {
        return team;
    }

    // ==================== SETTER ====================

    /**
     * Imposta lo stato della call (usato dal pattern State).
     *
     * @param statoCall Il nuovo stato
     */
    public void setStatoCall(IStatoCall statoCall) {
        this.statoCall = statoCall;
    }

    /**
     * Imposta la data e ora della call.
     *
     * @param dataOra La data e ora
     */
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    /**
     * Imposta la durata della call.
     *
     * @param durata La durata in minuti
     */
    public void setDurata(int durata) {
        this.durata = durata;
    }

    /**
     * Imposta le note della call.
     *
     * @param note Le note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Imposta l'ID della prenotazione nel sistema Calendar.
     *
     * @param idPrenotazione L'ID della prenotazione
     */
    public void setIdPrenotazione(String idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Conferma la call (il team accetta la proposta).
     * Delega l'operazione allo stato corrente (State Pattern).
     *
     * Precondizioni:
     * - Lo stato deve essere PROPOSTA
     *
     * Postcondizioni:
     * - Lo stato passa a CONFERMATA
     *
     * @throws IllegalStateException se l'operazione non e' permessa
     */
    public void conferma() {
        statoCall.conferma(this);
    }

    /**
     * Prenota la call con data e ora.
     * Delega l'operazione allo stato corrente (State Pattern).
     *
     * Precondizioni:
     * - Lo stato deve essere CONFERMATA
     *
     * Postcondizioni:
     * - Lo stato passa a PRENOTATA
     * - La data e ora vengono impostate
     *
     * @param dataOra La data e ora della prenotazione
     * @throws IllegalStateException se l'operazione non e' permessa
     */
    public void prenota(LocalDateTime dataOra) {
        statoCall.prenota(this, dataOra);
    }

    /**
     * Completa la call (la call e' stata effettuata).
     * Delega l'operazione allo stato corrente (State Pattern).
     *
     * Precondizioni:
     * - Lo stato deve essere PRENOTATA
     *
     * Postcondizioni:
     * - Lo stato passa a COMPLETATA
     *
     * @throws IllegalStateException se l'operazione non e' permessa
     */
    public void completa() {
        statoCall.completa(this);
    }

    /**
     * Annulla la call.
     * Delega l'operazione allo stato corrente (State Pattern).
     *
     * Precondizioni:
     * - Lo stato non deve essere COMPLETATA
     *
     * Postcondizioni:
     * - Lo stato passa ad ANNULLATA
     *
     * @throws IllegalStateException se l'operazione non e' permessa
     */
    public void annulla() {
        statoCall.annulla(this);
    }

    /**
     * Verifica se la call e' in stato proposta.
     *
     * @return true se lo stato e' PROPOSTA
     */
    public boolean isProposta() {
        return "PROPOSTA".equals(statoCall.getNomeStato());
    }

    /**
     * Verifica se la call e' confermata.
     *
     * @return true se lo stato e' CONFERMATA
     */
    public boolean isConfermata() {
        return "CONFERMATA".equals(statoCall.getNomeStato());
    }

    /**
     * Verifica se la call e' prenotata.
     *
     * @return true se lo stato e' PRENOTATA
     */
    public boolean isPrenotata() {
        return "PRENOTATA".equals(statoCall.getNomeStato());
    }

    /**
     * Verifica se la call e' completata.
     *
     * @return true se lo stato e' COMPLETATA
     */
    public boolean isCompletata() {
        return "COMPLETATA".equals(statoCall.getNomeStato());
    }

    /**
     * Verifica se la call e' annullata.
     *
     * @return true se lo stato e' ANNULLATA
     */
    public boolean isAnnullata() {
        return "ANNULLATA".equals(statoCall.getNomeStato());
    }

    /**
     * Restituisce una rappresentazione testuale della call.
     *
     * @return Stringa con info sulla call
     */
    @Override
    public String toString() {
        String info = "Call: " + mentore.getNome() + " -> " + team.getNome() + " [" + statoCall.getNomeStato() + "]";
        if (dataOra != null) {
            info += " - " + dataOra;
        }
        return info;
    }
}
