package it.unicam.ids.service;

import it.unicam.ids.model.Utente;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ObserverSupporto {

    private final List<Utente> subscribers = new ArrayList<>();
    private final NotificationService notificationService;

    public ObserverSupporto(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void iscrivi(Utente utente) {
        subscribers.add(utente);
    }

    public void notifySubscribers() {
        for (Utente utente : subscribers) {
            String email = utente.getEmail();
            Long utenteId = utente.getId();
            notificationService.update("Il mentore " + utenteId + " (" + email + ") ha una nuova richiesta di supporto");
        }
        subscribers.clear();
    }
}
