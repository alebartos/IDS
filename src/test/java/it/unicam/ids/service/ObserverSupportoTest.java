package it.unicam.ids.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverSupportoTest {

    @Test
    void testNotifySubscribers() {
        ObserverSupporto observer = new ObserverSupporto();
        List<String> messaggi = new ArrayList<>();

        Subscriber sub1 = messaggi::add;
        Subscriber sub2 = messaggi::add;

        observer.addSubscriber(sub1);
        observer.addSubscriber(sub2);

        observer.notify("Test messaggio");

        assertEquals(2, messaggi.size());
        assertEquals("Test messaggio", messaggi.get(0));
        assertEquals("Test messaggio", messaggi.get(1));
    }

    @Test
    void testRemoveSubscriber() {
        ObserverSupporto observer = new ObserverSupporto();
        List<String> messaggi = new ArrayList<>();

        Subscriber sub = messaggi::add;
        observer.addSubscriber(sub);
        observer.removeSubscriber(sub);

        observer.notify("Non dovrebbe arrivare");

        assertTrue(messaggi.isEmpty());
    }

    @Test
    void testNotifyWithNoSubscribers() {
        ObserverSupporto observer = new ObserverSupporto();
        assertDoesNotThrow(() -> observer.notify("Nessun subscriber"));
    }

    @Test
    void testNotificationServiceImplementation() {
        NotificationService notificationService = new NotificationService();
        assertDoesNotThrow(() -> notificationService.update("Test notifica"));
    }

    @Test
    void testConsoleServiceImplementation() {
        ConsoleService consoleService = new ConsoleService();
        assertDoesNotThrow(() -> consoleService.update("Test console"));
    }

    @Test
    void testObserverConImplementazioniReali() {
        ObserverSupporto observer = new ObserverSupporto();
        observer.addSubscriber(new NotificationService());
        observer.addSubscriber(new ConsoleService());

        assertDoesNotThrow(() -> observer.notify("Nuova richiesta di supporto"));
    }
}
