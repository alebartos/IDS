package com.hackhub.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un Giudice in HackHub.
 * <p>
 * Il Giudice:
 * - Valuta le sottomissioni dei team negli hackathon
 * - Assegna punteggi e giudizi
 * <p>
 * Estende: MembroStaff
 */
public class Giudice extends MembroStaff {

    /** Lista delle valutazioni espresse dal giudice */
    private List<Valutazione> valutazioniEspresse;

    /** Lista degli hackathon a cui il giudice e' assegnato */
    private List<Hackathon> hackathonAssegnati;

    /**
     * Costruttore della classe Giudice.
     *
     * @param nome     Il nome del giudice
     * @param cognome  Il cognome del giudice
     * @param email    L'email del giudice
     * @param password La password del giudice
     */
    public Giudice(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.setRuolo("Giudice");
        this.valutazioniEspresse = new ArrayList<>();
        this.hackathonAssegnati = new ArrayList<>();
    }

    // ==================== GETTER ====================

    /**
     * Restituisce la lista delle valutazioni espresse.
     *
     * @return Lista delle valutazioni
     */
    public List<Valutazione> getValutazioniEspresse() {
        return valutazioniEspresse;
    }

    /**
     * Restituisce la lista degli hackathon assegnati.
     *
     * @return Lista degli hackathon
     */
    public List<Hackathon> getHackathonAssegnati() {
        return hackathonAssegnati;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Valuta una sottomissione di un team.
     * <p>
     * Precondizioni:
     * - La sottomissione non deve essere gia' stata valutata
     * - La sottomissione deve essere definitiva
     * - Il giudice deve essere assegnato all'hackathon della sottomissione
     * <p>
     * Postcondizioni:
     * - Viene creata una nuova Valutazione
     * - La valutazione viene associata alla sottomissione
     * - La valutazione viene aggiunta alla lista del giudice
     *
     * @param sottomissione La sottomissione da valutare
     * @param punteggio     Il punteggio assegnato (es: 0-100)
     * @param giudizio      Il giudizio testuale
     * @return La valutazione creata
     * @throws IllegalStateException se la sottomissione e' gia' stata valutata
     * @throws IllegalStateException se la sottomissione non e' definitiva
     */
    public Valutazione valutaSottomissione(Sottomissione sottomissione, int punteggio, String giudizio) {
        // Verifica che non sia gia' stata valutata
        if (sottomissione.getValutazione() != null) {
            throw new IllegalStateException("La sottomissione e' gia' stata valutata");
        }

        // Verifica che sia definitiva
        if (!sottomissione.isDefinitiva()) {
            throw new IllegalStateException("La sottomissione non e' definitiva");
        }

        // Crea la valutazione
        Valutazione nuovaValutazione = new Valutazione(sottomissione, this, punteggio, giudizio);

        // Associa la valutazione alla sottomissione
        sottomissione.setValutazione(nuovaValutazione);

        // Aggiunge alla lista delle valutazioni espresse
        this.valutazioniEspresse.add(nuovaValutazione);

        return nuovaValutazione;
    }

    /**
     * Aggiunge un hackathon alla lista di quelli assegnati al giudice.
     *
     * @param hackathon L'hackathon da assegnare
     */
    public void aggiungiHackathon(Hackathon hackathon) {
        this.hackathonAssegnati.add(hackathon);
    }
}
