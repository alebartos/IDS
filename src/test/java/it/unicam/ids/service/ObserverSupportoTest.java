package it.unicam.ids.service;

import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverSupportoTest {

    @Test
    void testIscriviENotify() {
        NotificationService notificationService = new NotificationService();
        ObserverSupporto observer = new ObserverSupporto(notificationService);

        UtenteRepository utenteRepo = new UtenteRepository();
        Utente mentore1 = utenteRepo.add(new Utente("Mario", "Rossi", "mario@example.com", "pass"));
        Utente mentore2 = utenteRepo.add(new Utente("Luigi", "Verdi", "luigi@example.com", "pass"));

        observer.iscrivi(mentore1);
        observer.iscrivi(mentore2);

        assertDoesNotThrow(() -> observer.notifica());
    }

    @Test
    void testNotifyWithNoSubscribers() {
        NotificationService notificationService = new NotificationService();
        ObserverSupporto observer = new ObserverSupporto(notificationService);

        assertDoesNotThrow(() -> observer.notifica());
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
