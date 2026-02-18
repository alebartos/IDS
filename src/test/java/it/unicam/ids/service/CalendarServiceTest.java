package it.unicam.ids.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    @Test
    void testPrenotaCallSimulazione() {
        CalendarService calendarService = new CalendarService();

        assertDoesNotThrow(() -> calendarService.prenotaCall(
                "Supporto tecnico",
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                List.of("mario@example.com", "anna@example.com")));
    }

    @Test
    void testPrenotaCallListaVuota() {
        CalendarService calendarService = new CalendarService();

        assertDoesNotThrow(() -> calendarService.prenotaCall(
                "Call vuota",
                LocalDate.now(),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                List.of()));
    }

    @Test
    void testCostruttoreConGoogleCalendarAPI() {
        GoogleCalendarAPI api = new GoogleCalendarAPI("percorso/inesistente.json");
        CalendarService calendarService = new CalendarService(api);
        assertNotNull(calendarService);
    }

    @Test
    void testPrenotaCallConCredenzialiNonValide() {
        GoogleCalendarAPI api = new GoogleCalendarAPI("percorso/inesistente.json");
        CalendarService calendarService = new CalendarService(api);

        assertThrows(RuntimeException.class, () -> calendarService.prenotaCall(
                "Test call",
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                List.of("test@example.com")));
    }
}
