package it.unicam.ids.service;

public class NotificationService implements Subscriber {

    @Override
    public void update(String contesto) {
        System.out.println("[NOTIFICA] " + contesto);
    }
}
