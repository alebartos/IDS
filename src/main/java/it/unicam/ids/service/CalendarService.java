package it.unicam.ids.service;

import java.time.LocalDate;
import java.util.List;

public class CalendarService {

    public void prenotaCall(String descrizione, LocalDate dataInizio, LocalDate dataFine, List<String> listaMail) {
        System.out.println("[CALENDAR] Prenotazione call: " + descrizione);
        System.out.println("[CALENDAR] Da: " + dataInizio + " A: " + dataFine);
        System.out.println("[CALENDAR] Partecipanti: " + listaMail);
    }
}
