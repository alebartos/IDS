package com.hackhub.model;

import com.hackhub.enums.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un Organizzatore di hackathon in HackHub.
 *
 * L'Organizzatore puo':
 * - Creare nuovi hackathon
 * - Gestire gli hackathon creati
 * - Proclamare il vincitore di un hackathon
 *
 * Estende: MembroStaff
 */
public class Organizzatore extends MembroStaff {

    /** Lista degli hackathon gestiti da questo organizzatore */
    private List<Hackathon> hackathonGestiti;

    /**
     * Costruttore della classe Organizzatore.
     *
     * @param nome     Il nome dell'organizzatore
     * @param cognome  Il cognome dell'organizzatore
     * @param email    L'email dell'organizzatore
     * @param password La password dell'organizzatore
     */
    public Organizzatore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.setRuolo("Organizzatore");
        this.hackathonGestiti = new ArrayList<>();
    }

    // ==================== GETTER ====================

    /**
     * Restituisce la lista degli hackathon gestiti.
     *
     * @return Lista degli hackathon
     */
    public List<Hackathon> getHackathonGestiti() {
        return hackathonGestiti;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Crea un nuovo hackathon.
     *
     * Precondizioni:
     * - Il nome dell'hackathon non deve essere gia' in uso
     * - La data di inizio deve precedere la data di fine
     * - La scadenza iscrizioni deve precedere la data di inizio
     *
     * Postcondizioni:
     * - Viene creato un nuovo Hackathon con stato IN_ISCRIZIONE
     * - L'organizzatore viene associato all'hackathon
     *
     * @param nome               Nome dell'hackathon
     * @param dataInizio         Data di inizio
     * @param dataFine           Data di fine
     * @param scadenzaIscrizioni Scadenza per le iscrizioni
     * @return L'hackathon creato
     * @throws IllegalArgumentException se le date non sono valide
     */
    public Hackathon creaHackathon(String nome, LocalDate dataInizio, LocalDate dataFine,
                                    LocalDate scadenzaIscrizioni) {
        // Validazione date
        if (dataInizio.isAfter(dataFine)) {
            throw new IllegalArgumentException("La data di inizio deve precedere la data di fine");
        }


        if (scadenzaIscrizioni.isAfter(dataInizio)) {
            throw new IllegalArgumentException("La scadenza iscrizioni deve precedere la data di inizio");
        }


    // Crea l'hackathon
        Hackathon nuovoHackathon = new Hackathon(nome, dataInizio, dataFine, scadenzaIscrizioni);
        nuovoHackathon.setOrganizzatore(this);


    // Aggiunge alla lista degli hackathon gestiti
        this.hackathonGestiti.add(nuovoHackathon);


        return nuovoHackathon;
    }

    /**
     * Proclama il vincitore di un hackathon.
     *
     * Precondizioni:
     * - L'hackathon deve essere in stato IN_VALUTAZIONE
     * - L'organizzatore deve gestire questo hackathon
     * - Tutte le sottomissioni devono essere state valutate
     *
     * Postcondizioni:
     * - Il team viene proclamato vincitore
     * - L'hackathon passa allo stato CONCLUSO
     *
     * @param team      Il team vincitore
     * @param hackathon L'hackathon in cui proclamare il vincitore
     * @throws IllegalStateException se l'hackathon non e' in stato IN_VALUTAZIONE
     * @throws IllegalArgumentException se l'organizzatore non gestisce questo hackathon
     */
    public void proclamaVincitore(Team team, Hackathon hackathon) {
        // Verifica che l'organizzatore gestisca questo hackathon
        if (!this.hackathonGestiti.contains(hackathon)) {
            throw new IllegalArgumentException("Non gestisci questo hackathon");
        }


    // Verifica stato hackathon
        if (hackathon.getStato() != StatoHackathon.IN_VALUTAZIONE) {
            throw new IllegalStateException("L'hackathon non e' in fase di valutazione");
        }


    // Verifica che tutte le sottomissioni siano valutate
        for (Sottomissione sottomissione : hackathon.getSottomissioni()) {
            if (sottomissione.getValutazione() == null) {
                throw new IllegalStateException("Ci sono sottomissioni non ancora valutate");
            }
        }


    // Proclama il vincitore
        hackathon.setVincitore(team);
        hackathon.setStato(StatoHackathon.CONCLUSO);
    }

    /**
     * Aggiunge un hackathon alla lista di quelli gestiti.
     *
     * @param hackathon L'hackathon da aggiungere
     */
    public void aggiungiHackathon(Hackathon hackathon) {
        this.hackathonGestiti.add(hackathon);
    }
}
