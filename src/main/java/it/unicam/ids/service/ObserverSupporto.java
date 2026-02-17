package it.unicam.ids.service;

import java.util.ArrayList;
import java.util.List;

public class ObserverSupporto {

    private final List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notify(String contesto) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(contesto);
        }
    }
}
