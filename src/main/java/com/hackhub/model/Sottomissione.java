package com.hackhub.model;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una Sottomissione di un progetto in HackHub.
 * <p>
 * Una Sottomissione è il progetto presentato da un Team per un Hackathon.
 * Caratteristiche:
 * - Puo' essere modificata finchè non è definitiva
 * - Una volta definitiva, puo' essere valutata dal Giudice
 * <p>
 * Relazioni:
 * - Composizione con Team (la sottomissione non esiste senza team)
 * - Associazione con Hackathon
 * - Composizione con Valutazione (la valutazione non esiste senza sottomissione)
 */
public class Sottomissione {

    /** Identificativo univoco della sottomissione */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Titolo del progetto */
    private String titolo;

    /** Descrizione del progetto */
    private String descrizione;

    /** URL del repository o del progetto */
    private String urlProgetto;

    /** Data e ora della consegna */
    private LocalDateTime dataConsegna;

    /** Indica se la sottomissione è definitiva */
    private boolean definitiva;

    /** Team che ha sottomesso il progetto */
    private Team team;

    /** Hackathon per cui è stata fatta la sottomissione */
    private Hackathon hackathon;

    /** Valutazione ricevuta (null se non ancora valutata) */
    private Valutazione valutazione;

    /**
     * Costruttore della classe Sottomissione.
     *
     * @param team      Il team che sottomette
     * @param hackathon L'hackathon per cui si sottomette
     * @param titolo    Il titolo del progetto
     */
    public Sottomissione(Team team, Hackathon hackathon, String titolo) {
        this.id = contatoreId++;
        this.team = team;
        this.hackathon = hackathon;
        this.titolo = titolo;
        this.dataConsegna = LocalDateTime.now();
        this.definitiva = false;
        this.valutazione = null;
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID della sottomissione.
     *
     * @return L'ID della sottomissione
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce il titolo del progetto.
     *
     * @return Il titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la descrizione del progetto.
     *
     * @return La descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce l'URL del progetto.
     *
     * @return L'URL del progetto
     */
    public String getUrlProgetto() {
        return urlProgetto;
    }

    /**
     * Restituisce la data di consegna.
     *
     * @return La data di consegna
     */
    public LocalDateTime getDataConsegna() {
        return dataConsegna;
    }

    /**
     * Verifica se la sottomissione è definitiva.
     *
     * @return true se definitiva, false altrimenti
     */
    public boolean isDefinitiva() {
        return definitiva;
    }

    /**
     * Restituisce il team che ha sottomesso.
     *
     * @return Il team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Restituisce l'hackathon per cui è stata fatta la sottomissione.
     *
     * @return L'hackathon
     */
    public Hackathon getHackathon() {
        return hackathon;
    }

    /**
     * Restituisce la valutazione ricevuta.
     *
     * @return La valutazione, o null se non ancora valutata
     */
    public Valutazione getValutazione() {
        return valutazione;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il titolo del progetto.
     *
     * @param titolo Il nuovo titolo
     * @throws IllegalStateException se la sottomissione è già definitiva
     */
    public void setTitolo(String titolo) {
        verificaNonDefinitiva();
        this.titolo = titolo;
    }

    /**
     * Imposta la descrizione del progetto.
     *
     * @param descrizione La nuova descrizione
     * @throws IllegalStateException se la sottomissione è già definitiva
     */
    public void setDescrizione(String descrizione) {
        verificaNonDefinitiva();
        this.descrizione = descrizione;
    }

    /**
     * Imposta l'URL del progetto.
     *
     * @param urlProgetto Il nuovo URL
     * @throws IllegalStateException se la sottomissione è già definitiva
     */
    public void setUrlProgetto(String urlProgetto) {
        verificaNonDefinitiva();
        this.urlProgetto = urlProgetto;
    }

    /**
     * Rende la sottomissione definitiva.
     *
     * @param definitiva true per rendere definitiva
     */
    public void setDefinitiva(boolean definitiva) {
        this.definitiva = definitiva;
        if (definitiva) {
            this.dataConsegna = LocalDateTime.now();
        }
    }

    /**
     * Imposta la valutazione della sottomissione.
     *
     * @param valutazione La valutazione da associare
     */
    public void setValutazione(Valutazione valutazione) {
        this.valutazione = valutazione;
    }

    // ==================== OPERAZIONI PRIVATE ====================

    /**
     * Verifica che la sottomissione non sia definitiva.
     *
     * @throws IllegalStateException se la sottomissione è già definitiva
     */
    private void verificaNonDefinitiva() {
        if (this.definitiva) {
            throw new IllegalStateException("La sottomissione è già definitiva e non puo' essere modificata");
        }
    }

    /**
     * Restituisce una rappresentazione testuale della sottomissione.
     *
     * @return Stringa con info sulla sottomissione
     */
    @Override
    public String toString() {
        String stato = definitiva ? "DEFINITIVA" : "BOZZA";
        return "Sottomissione: " + titolo + " [" + stato + "] - Team: " + team.getNome();
    }
}
