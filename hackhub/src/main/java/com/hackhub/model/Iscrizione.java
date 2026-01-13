package com.hackhub.model;

import com.hackhub.enums.StatoIscrizione;
import java.time.LocalDate;

/**
 * Classe che rappresenta l'iscrizione di un Team a un Hackathon.
 * <p>
 * Un'iscrizione viene creata quando un Leader iscrive il proprio team
 * a un hackathon. Lo stato iniziale e' CONFERMATA.
 * <p>
 * Relazioni:
 * - Composizione con Team (l'iscrizione non esiste senza team)
 * - Associazione con Hackathon
 */
public class Iscrizione {

    /** Identificativo univoco dell'iscrizione */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Data di iscrizione */
    private LocalDate dataIscrizione;

    /** Stato corrente dell'iscrizione */
    private StatoIscrizione stato;

    /** Team iscritto */
    private Team team;

    /** Hackathon a cui il team e' iscritto */
    private Hackathon hackathon;

    /**
     * Costruttore della classe Iscrizione.
     *
     * @param team      Il team che si iscrive
     * @param hackathon L'hackathon a cui iscriversi
     */
    public Iscrizione(Team team, Hackathon hackathon) {
        this.id = contatoreId++;
        this.team = team;
        this.hackathon = hackathon;
        this.dataIscrizione = LocalDate.now();
        this.stato = StatoIscrizione.CONFERMATA;
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID dell'iscrizione.
     *
     * @return L'ID dell'iscrizione
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce la data di iscrizione.
     *
     * @return La data di iscrizione
     */
    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    /**
     * Restituisce lo stato dell'iscrizione.
     *
     * @return Lo stato corrente
     */
    public StatoIscrizione getStato() {
        return stato;
    }

    /**
     * Restituisce il team iscritto.
     *
     * @return Il team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Restituisce l'hackathon a cui il team e' iscritto.
     *
     * @return L'hackathon
     */
    public Hackathon getHackathon() {
        return hackathon;
    }

    // ==================== SETTER ====================

    /**
     * Imposta lo stato dell'iscrizione.
     *
     * @param stato Il nuovo stato
     */
    public void setStato(StatoIscrizione stato) {
        this.stato = stato;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Annulla l'iscrizione.
     * <p>
     * Postcondizioni:
     * - Lo stato passa ad ANNULLATA
     */
    public void annulla() {
        this.stato = StatoIscrizione.ANNULLATA;
    }

    /**
     * Restituisce una rappresentazione testuale dell'iscrizione.
     *
     * @return Stringa con info sull'iscrizione
     */
    @Override
    public String toString() {
        return "Iscrizione di " + team.getNome() + " a " + hackathon.getNome() + " [" + stato + "]";
    }
}
