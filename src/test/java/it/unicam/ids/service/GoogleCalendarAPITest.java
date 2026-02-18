package it.unicam.ids.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GoogleCalendarAPITest {

    @Test
    void testCostruttore() {
        GoogleCalendarAPI api = new GoogleCalendarAPI("credentials.json");
        assertNotNull(api);
    }

    @Test
    void testGetCalendarServiceSenzaCredenziali() {
        GoogleCalendarAPI api = new GoogleCalendarAPI("file/che/non/esiste.json");

        assertThrows(IOException.class, () -> api.getCalendarService());
    }
}
