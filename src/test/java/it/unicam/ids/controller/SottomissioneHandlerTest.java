package it.unicam.ids.controller;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.service.SottomissioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SottomissioneHandlerTest {

    private SottomissioneHandler sottomissioneHandler;
    private SottomissioneRepository sottomissioneRepository;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
        SottomissioneService sottomissioneService = new SottomissioneService(sottomissioneRepository);
        sottomissioneHandler = new SottomissioneHandler(sottomissioneService);
    }

    @Test
    void testCaricaBozzaSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(1L, 1L, dati, false);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getData());
        assertEquals(StatoSottomissione.BOZZA, result.getData().getStato());
    }

    @Test
    void testCaricaBozzaDefinitiva() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(1L, 1L, dati, true);

        assertTrue(result.isSuccess());
        assertEquals(StatoSottomissione.CONSEGNATA, result.getData().getStato());
    }

    @Test
    void testCaricaBozzaGiaConsegnata() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");
        sottomissioneHandler.caricaBozza(1L, 1L, dati, true);

        Result<Sottomissione> result = sottomissioneHandler.caricaBozza(1L, 1L, dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerSuccess() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(1L, 1L, dati, false);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerDefinitiva() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "https://github.com/repo");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(1L, 1L, dati, true);

        assertTrue(result.isSuccess());
        assertEquals(StatoSottomissione.CONSEGNATA, result.getData().getStato());
    }

    @Test
    void testSottomissioneHandlerLinkNonValido() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "link-non-valido");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(1L, 1L, dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testSottomissioneHandlerLinkVuoto() {
        DatiProgetto dati = new DatiProgetto("Titolo", "Desc", "");

        Result<Sottomissione> result = sottomissioneHandler.sottomissioneHandler(1L, 1L, dati, false);

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
