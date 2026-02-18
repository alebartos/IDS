package it.unicam.ids.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service per la gestione del calendario tramite Google Calendar API.
 * Supporta due modalità:
 * - Con GoogleCalendarAPI: crea eventi reali su Google Calendar
 * - Senza GoogleCalendarAPI (null): modalità simulazione per test e demo
 */
public class CalendarService {

    private final GoogleCalendarAPI googleCalendarAPI;

    /**
     * Costruttore per modalità simulazione (senza Google Calendar).
     * Usato nei test e quando le credenziali non sono disponibili.
     */
    public CalendarService() {
        this.googleCalendarAPI = null;
    }

    /**
     * Costruttore per modalità reale con Google Calendar API.
     * @param googleCalendarAPI istanza configurata di GoogleCalendarAPI
     */
    public CalendarService(GoogleCalendarAPI googleCalendarAPI) {
        this.googleCalendarAPI = googleCalendarAPI;
    }

    /**
     * Prenota una call creando un evento su Google Calendar.
     * Se GoogleCalendarAPI non è configurato, opera in modalità simulazione.
     *
     * @param descrizione descrizione della call
     * @param data data della call
     * @param oraInizio orario di inizio della call
     * @param oraFine orario di fine della call
     * @param listaMail lista email dei partecipanti
     */
    public void prenotaCall(String descrizione, LocalDate data, LocalTime oraInizio, LocalTime oraFine, List<String> listaMail) {
        if (googleCalendarAPI == null) {
            System.out.println("[CALENDAR - SIMULAZIONE] Prenotazione call: " + descrizione);
            System.out.println("[CALENDAR - SIMULAZIONE] Data: " + data + " Ore: " + oraInizio + " - " + oraFine);
            System.out.println("[CALENDAR - SIMULAZIONE] Partecipanti: " + listaMail);
            return;
        }

        try {
            Calendar service = googleCalendarAPI.getCalendarService();

            Event event = new Event()
                    .setSummary("Supporto Hackathon: " + descrizione)
                    .setDescription("Call di supporto prenotata tramite il sistema IDS Hackathon.\n" +
                            "Descrizione: " + descrizione);

            DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
            String startStr = data + "T" + oraInizio.format(timeFmt);
            String endStr = data + "T" + oraFine.format(timeFmt);

            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(startStr))
                    .setTimeZone("Europe/Rome");
            event.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(endStr))
                    .setTimeZone("Europe/Rome");
            event.setEnd(end);

            List<EventAttendee> attendees = listaMail.stream()
                    .map(email -> new EventAttendee().setEmail(email))
                    .collect(Collectors.toList());
            event.setAttendees(attendees);

            // Chiamata come da diagramma di sequenza:
            // service.events().insert("primary", Event).setSendUpdates("all").execute
            Event createdEvent = service.events()
                    .insert("primary", event)
                    .setSendUpdates("all")
                    .execute();

            System.out.println("[CALENDAR] Evento creato con successo!");
            System.out.println("[CALENDAR] ID Evento: " + createdEvent.getId());
            System.out.println("[CALENDAR] Link: " + createdEvent.getHtmlLink());

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("[CALENDAR] Errore nella creazione dell'evento: " + e.getMessage());
            throw new RuntimeException("Errore nella prenotazione della call su Google Calendar", e);
        }
    }

    /**
     * Restituisce la lista degli eventi futuri dal calendario primario.
     * In modalità simulazione restituisce una lista di eventi fittizi.
     *
     * @return lista di eventi del calendario
     */
    public List<Event> getEventi() {
        if (googleCalendarAPI == null) {
            System.out.println("[CALENDAR - SIMULAZIONE] Recupero lista eventi dal calendario primario");
            List<Event> eventiSimulati = new ArrayList<>();
            Event evento = new Event()
                    .setSummary("Supporto Hackathon: evento simulato")
                    .setId("simulated-event-1");
            eventiSimulati.add(evento);
            return eventiSimulati;
        }

        try {
            Calendar service = googleCalendarAPI.getCalendarService();
            DateTime now = new DateTime(System.currentTimeMillis());
            List<Event> events = service.events().list("primary")
                    .setTimeMin(now)
                    .execute()
                    .getItems();
            return events != null ? events : new ArrayList<>();
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("[CALENDAR] Errore nel recupero degli eventi: " + e.getMessage());
            throw new RuntimeException("Errore nel recupero degli eventi dal calendario", e);
        }
    }

    /**
     * Conferma la partecipazione a un evento del calendario,
     * impostando responseStatus="accepted" per l'attendee corrente.
     * In modalità simulazione stampa un messaggio.
     *
     * @param eventId l'ID dell'evento a cui confermare la partecipazione
     */
    public void confermaPartecipazione(String eventId) {
        if (googleCalendarAPI == null) {
            System.out.println("[CALENDAR - SIMULAZIONE] Conferma partecipazione all'evento: " + eventId);
            System.out.println("[CALENDAR - SIMULAZIONE] PATCH event con responseStatus=accepted, sendUpdates=all");
            return;
        }

        try {
            Calendar service = googleCalendarAPI.getCalendarService();

            // GET events.get(primary, eventId)
            Event event = service.events().get("primary", eventId).execute();

            // Imposta responseStatus="accepted" per l'attendee self
            if (event.getAttendees() != null) {
                for (EventAttendee attendee : event.getAttendees()) {
                    if (Boolean.TRUE.equals(attendee.getSelf())) {
                        attendee.setResponseStatus("accepted");
                    }
                }
            }

            // PATCH events.patch(primary, eventId, event, sendUpdates=all)
            service.events().patch("primary", eventId, event)
                    .setSendUpdates("all")
                    .execute();

            System.out.println("[CALENDAR] Partecipazione confermata per l'evento: " + eventId);

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("[CALENDAR] Errore nella conferma partecipazione: " + e.getMessage());
            throw new RuntimeException("Errore nella conferma della partecipazione", e);
        }
    }
}
