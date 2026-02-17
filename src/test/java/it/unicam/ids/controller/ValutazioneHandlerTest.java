package it.unicam.ids.controller;

import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.service.ValutazioneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValutazioneHandlerTest {

    private ValutazioneHandler valutazioneHandler;
    private SottomissioneRepository sottomissioneRepository;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
        ValutazioneService valutazioneService = new ValutazioneService(sottomissioneRepository);
        valutazioneHandler = new ValutazioneHandler(valutazioneService);
    }

    @Test
    void testConfermaSottomissioneSuccess() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        s.setStato(StatoSottomissione.CONSEGNATA);
        s = sottomissioneRepository.add(s);

        Result<Sottomissione> result = valutazioneHandler.confermaSottomissione(
                1L, s.getId(), 85, "Ottimo lavoro");

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getData());
        assertEquals(StatoSottomissione.VALUTATA, result.getData().getStato());
    }

    @Test
    void testConfermaSottomissioneNonTrovata() {
        Result<Sottomissione> result = valutazioneHandler.confermaSottomissione(
                1L, 999L, 80, "Buono");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testConfermaSottomissioneGiaValutata() {
        Sottomissione s = new Sottomissione();
        s.setStato(StatoSottomissione.VALUTATA);
        s = sottomissioneRepository.add(s);

        Result<Sottomissione> result = valutazioneHandler.confermaSottomissione(
                1L, s.getId(), 80, "Buono");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testConfermaSottomissioneGiudizioVuoto() {
        Sottomissione s = new Sottomissione();
        s.setStato(StatoSottomissione.CONSEGNATA);
        s = sottomissioneRepository.add(s);

        Result<Sottomissione> result = valutazioneHandler.confermaSottomissione(
                1L, s.getId(), 80, "");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }

    @Test
    void testConfermaSottomissioneParametriNull() {
        Result<Sottomissione> result = valutazioneHandler.confermaSottomissione(
                null, 1L, 80, "Buono");

        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
    }
}
