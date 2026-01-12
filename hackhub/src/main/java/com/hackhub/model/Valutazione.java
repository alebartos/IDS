package com.hackhub.model;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una Valutazione di una Sottomissione in HackHub.
 *
 * Una Valutazione viene espressa da un Giudice su una Sottomissione definitiva.
 * Contiene un punteggio numerico e un giudizio testuale.
 *
 * Relazioni:
 * - Composizione con Sottomissione (la valutazione non esiste senza sottomissione)
 * - Associazione con Giudice
 */
public class Valutazione {

    /** Identificativo univoco della valutazione */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Punteggio assegnato (es: 0-100) */
    private int punteggio;

    /** Giudizio testuale */
    private String giudizio;

    /** Data e ora della valutazione */
    private LocalDateTime dataValutazione;

    /** Sottomissione valutata */
    private Sottomissione sottomissione;

    /** Giudice che ha espresso la valutazione */
    private Giudice giudice;

    /**
     * Costruttore della classe Valutazione.
     *
     * @param sottomissione La sottomissione da valutare
     * @param giudice       Il giudice che valuta
     * @param punteggio     Il punteggio assegnato
     * @param giudizio      Il giudizio testuale
     */
    public Valutazione(Sottomissione sottomissione, Giudice giudice, int punteggio, String giudizio) {
        this.id = contatoreId++;
        this.sottomissione = sottomissione;
        this.giudice = giudice;
        this.punteggio = punteggio;
        this.giudizio = giudizio;
        this.dataValutazione = LocalDateTime.now();
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID della valutazione.
     *
     * @return L'ID della valutazione
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce il punteggio assegnato.
     *
     * @return Il punteggio
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Restituisce il giudizio testuale.
     *
     * @return Il giudizio
     */
    public String getGiudizio() {
        return giudizio;
    }

    /**
     * Restituisce la data e ora della valutazione.
     *
     * @return La data di valutazione
     */
    public LocalDateTime getDataValutazione() {
        return dataValutazione;
    }

    /**
     * Restituisce la sottomissione valutata.
     *
     * @return La sottomissione
     */
    public Sottomissione getSottomissione() {
        return sottomissione;
    }

    /**
     * Restituisce il giudice che ha espresso la valutazione.
     *
     * @return Il giudice
     */
    public Giudice getGiudice() {
        return giudice;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il punteggio.
     *
     * @param punteggio Il nuovo punteggio
     */
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Imposta il giudizio.
     *
     * @param giudizio Il nuovo giudizio
     */
    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    /**
     * Restituisce una rappresentazione testuale della valutazione.
     *
     * @return Stringa con info sulla valutazione
     */
    @Override
    public String toString() {
        return "Valutazione: " + punteggio + "/100 - " + giudizio;
    }
}
