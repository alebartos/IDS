package it.unicam.ids.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Subscriber {

    @Override
    public void update(String messaggio) {
        System.out.println("[NOTIFICA] " + messaggio);
        System.out.println("[EMAIL] Invio email: " + messaggio);
    }
}
