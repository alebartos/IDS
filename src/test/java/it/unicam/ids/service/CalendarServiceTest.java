package it.unicam.ids.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    @Test
    void testPrenotaCall() {
        CalendarService calendarService = new CalendarService();

        assertDoesNotThrow(() -> calendarService.prenotaCall(
                "Supporto tecnico",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                List.of("mario@example.com", "anna@example.com")));
    }

    @Test
    void testPrenotaCallListaVuota() {
        CalendarService calendarService = new CalendarService();

        assertDoesNotThrow(() -> calendarService.prenotaCall(
                "Call vuota",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of()));
    }
}
