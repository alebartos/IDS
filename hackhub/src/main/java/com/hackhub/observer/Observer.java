package com.hackhub.observer;

/**
 * Interfaccia Observer per il pattern Observer.
 * <p>
 * Gli oggetti che implementano questa interfaccia possono registrarsi
 * presso un Subject (es: NotificationService) per ricevere notifiche
 * quando si verificano eventi nel sistema.
 * <p>
 * Design Pattern: Observer
 */
public interface Observer {

    /**
     * Metodo chiamato quando l'Observer viene notificato di un evento.
     *
     * @param messaggio Il messaggio di notifica
     */
    void update(String messaggio);
}
