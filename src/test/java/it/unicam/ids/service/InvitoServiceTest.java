package it.unicam.ids.service;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InvitoServiceTest {

    @Autowired
    private InvitoService invitoService;

    @Autowired
    private InvitoRepository invitoRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    private Utente destinatario;

    @BeforeEach
    void setUp() {
        destinatario = new Utente("Anna", "Bianchi", "anna.bianchi@example.com", "password456");
        destinatario = utenteRepository.save(destinatario);
    }

    @Test
    void testInvitaMembroSuccess() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com");

        assertNotNull(invito);
        assertNotNull(invito.getId());
        assertEquals(destinatario.getId(), invito.getDestinatario());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testInvitaMembroUtenteNonTrovato() {
        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("nonexistent@example.com"));
    }

    @Test
    void testInvitaMembroDuplicato() {
        invitoService.invitaMembro("anna.bianchi@example.com");

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.invitaMembro("anna.bianchi@example.com"));
    }

    @Test
    void testGestisciInvitoAccettato() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com");

        invitoService.gestisciInvito(invito.getId(), "ACCETTATO");

        Invito aggiornato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.ACCETTATO, aggiornato.getStato());
    }

    @Test
    void testGestisciInvitoRifiutato() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com");

        invitoService.gestisciInvito(invito.getId(), "RIFIUTATO");

        Invito aggiornato = invitoRepository.findById(invito.getId()).orElseThrow();
        assertEquals(StatoInvito.RIFIUTATO, aggiornato.getStato());
    }

    @Test
    void testGestisciInvitoRispostaNonValida() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com");

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.gestisciInvito(invito.getId(), "INVALID"));
    }

    @Test
    void testGestisciInvitoGiaGestito() {
        Invito invito = invitoService.invitaMembro("anna.bianchi@example.com");

        invitoService.gestisciInvito(invito.getId(), "ACCETTATO");

        assertThrows(IllegalArgumentException.class,
                () -> invitoService.gestisciInvito(invito.getId(), "ACCETTATO"));
    }
}
