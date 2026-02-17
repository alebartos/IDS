package it.unicam.ids.service;

public class ConsoleService implements Subscriber {

    @Override
    public void update(String contesto) {
        System.out.println("[CONSOLE] " + contesto);
    }
}
