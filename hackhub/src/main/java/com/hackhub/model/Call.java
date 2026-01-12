package com.hackhub.model;

import com.hackhub.enums.StatoCall;
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
 */
public class Call {

    /** Identificativo univoco della call */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Stato corrente della call */
    private StatoCall stato;

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
        this.stato = StatoCall.PROPOSTA;
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
    public StatoCall getStato() {
        return stato;
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
     * Imposta lo stato della call.
     *
     * @param stato Il nuovo stato
     */
    public void setStato(StatoCall stato) {
        this.stato = stato;
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
     *
     * Precondizioni:
     * - Lo stato deve essere PROPOSTA
     *
     * Postcondizioni:
     * - Lo stato passa a CONFERMATA
     *
     * @throws IllegalStateException se lo stato non e' PROPOSTA
     */
    public void conferma() {
        if (this.stato != StatoCall.PROPOSTA) {
            throw new IllegalStateException("La call non e' in stato PROPOSTA");
        }
        this.stato = StatoCall.CONFERMATA;
    }

    /**
     * Annulla la call.
     *
     * Precondizioni:
     * - Lo stato non deve essere COMPLETATA
     *
     * Postcondizioni:
     * - Lo stato passa ad ANNULLATA
     *
     * @throws IllegalStateException se la call e' gia' completata
     */
    public void annulla() {
        if (this.stato == StatoCall.COMPLETATA) {
            throw new IllegalStateException("Non puoi annullare una call gia' completata");
        }
        this.stato = StatoCall.ANNULLATA;
    }

    /**
     * Completa la call (la call e' stata effettuata).
     *
     * Precondizioni:
     * - Lo stato deve essere PRENOTATA
     *
     * Postcondizioni:
     * - Lo stato passa a COMPLETATA
     *
     * @throws IllegalStateException se lo stato non e' PRENOTATA
     */
    public void completa() {
        if (this.stato != StatoCall.PRENOTATA) {
            throw new IllegalStateException("La call non e' in stato PRENOTATA");
        }
        this.stato = StatoCall.COMPLETATA;
    }

    /**
     * Restituisce una rappresentazione testuale della call.
     *
     * @return Stringa con info sulla call
     */
    @Override
    public String toString() {
        String info = "Call: " + mentore.getNome() + " -> " + team.getNome() + " [" + stato + "]";
        if (dataOra != null) {
            info += " - " + dataOra;
        }
        return info;
    }
}
