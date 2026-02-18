package it.unicam.ids.service;

import it.unicam.ids.model.Utente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverSupportoTest {

    @Test
    void testIscriviENotify() {
        NotificationService notificationService = new NotificationService();
        ObserverSupporto observer = new ObserverSupporto(notificationService);

        Utente mentore1 = new Utente("Mario", "Rossi", "mario@example.com", "pass");
        mentore1.setId(1L);
        Utente mentore2 = new Utente("Luigi", "Verdi", "luigi@example.com", "pass");
        mentore2.setId(2L);

        observer.iscrivi(mentore1);
        observer.iscrivi(mentore2);

        assertDoesNotThrow(() -> observer.notifySubscribers());
    }

    @Test
    void testNotifyWithNoSubscribers() {
        NotificationService notificationService = new NotificationService();
        ObserverSupporto observer = new ObserverSupporto(notificationService);

        assertDoesNotThrow(() -> observer.notifySubscribers());
    }

    @Test
    void testNotificationServiceImplementation() {
        NotificationService notificationService = new NotificationService();
        assertDoesNotThrow(() -> notificationService.update("Test notifica"));
    }

    @Test
    void testNotificationServiceImplementsSubscriber() {
        NotificationService notificationService = new NotificationService();
        assertTrue(notificationService instanceof Subscriber);
    }
}
