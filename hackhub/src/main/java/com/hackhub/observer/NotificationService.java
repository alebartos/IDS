package com.hackhub.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Servizio di notifica che implementa il pattern Observer.
 * <p>
 * Questa classe funge da Subject nel pattern Observer, permettendo
 * agli Observer di registrarsi e ricevere notifiche quando si
 * verificano eventi nel sistema HackHub.
 * <p>
 * Eventi tipici:
 * - Nuovo invito ricevuto
 * - Invito accettato/rifiutato
 * - Iscrizione a hackathon
 * - Nuova sottomissione
 * - Valutazione ricevuta
 * - Call proposta/confermata
 * <p>
 * Design Pattern: Observer (Subject)
 */
public class NotificationService {

    /** Lista degli observer registrati */
    private List<Observer> observers;

    /**
     * Costruttore del servizio di notifica.
     * Inizializza la lista degli observer.
     */
    public NotificationService() {
        this.observers = new ArrayList<>();
    }

    /**
     * Registra un nuovo observer per ricevere notifiche.
     *
     * @param observer L'observer da registrare
     */
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Rimuove un observer dalla lista delle notifiche.
     *
     * @param observer L'observer da rimuovere
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica tutti gli observer registrati con un messaggio.
     *
     * @param messaggio Il messaggio da inviare a tutti gli observer
     */
    public void aggiornaObservers(String messaggio) {
        for (Observer observer : observers) {
            observer.update(messaggio);
        }
    }

    /**
     * Restituisce il numero di observer registrati.
     *
     * @return Il numero di observer
     */
    public int countObservers() {
        return observers.size();
    }

    /**
     * Verifica se un observer è registrato.
     *
     * @param observer L'observer da verificare
     * @return true se l'observer è registrato, false altrimenti
     */
    public boolean hasObserver(Observer observer) {
        return observers.contains(observer);
    }

    /**
     * Rimuove tutti gli observer registrati.
     */
    public void clearObservers() {
        observers.clear();
    }
}
