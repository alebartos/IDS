package it.unicam.ids.service;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.SottomissioneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValutazioneServiceTest {

    private ValutazioneService valutazioneService;
    private SottomissioneRepository sottomissioneRepository;

    @BeforeEach
    void setUp() {
        sottomissioneRepository = new SottomissioneRepository();
        valutazioneService = new ValutazioneService(sottomissioneRepository);
    }

    @Test
    void testCreaDTO() {
        DatiValutazione dv = valutazioneService.creaDTO(85, "Ottimo lavoro");
        assertEquals(85, dv.getPunteggio());
        assertEquals("Ottimo lavoro", dv.getGiudizio());
        assertNotNull(dv.getDataCreazione());
    }

    @Test
    void testCheckValiditàSuccess() {
        DatiValutazione dv = new DatiValutazione(80, "Buono", java.time.LocalDate.now());
        assertDoesNotThrow(() -> valutazioneService.checkValidità(dv));
    }

    @Test
    void testCheckValiditàNull() {
        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.checkValidità(null));
    }

    @Test
    void testCheckValiditàGiudizioNull() {
        DatiValutazione dv = new DatiValutazione(80, null, java.time.LocalDate.now());
        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.checkValidità(dv));
    }

    @Test
    void testCheckValiditàGiudizioVuoto() {
        DatiValutazione dv = new DatiValutazione(80, "", java.time.LocalDate.now());
        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.checkValidità(dv));
    }

    @Test
    void testValutaSottomissioneSuccess() {
        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(1L);
        s.setStato(StatoSottomissione.CONSEGNATA);
        s = sottomissioneRepository.add(s);

        DatiValutazione dv = valutazioneService.creaDTO(90, "Eccellente");
        Sottomissione valutata = valutazioneService.valutaSottomissione(1L, s.getId(), dv);

        assertEquals(StatoSottomissione.VALUTATA, valutata.getStato());
        assertNotNull(valutata.getDatiValutazione());
        assertEquals(90, valutata.getDatiValutazione().getPunteggio());
    }

    @Test
    void testValutaSottomissioneGiaValutata() {
        Sottomissione s = new Sottomissione();
        s.setStato(StatoSottomissione.VALUTATA);
        s = sottomissioneRepository.add(s);

        DatiValutazione dv = valutazioneService.creaDTO(80, "Buono");
        Long sId = s.getId();

        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(1L, sId, dv));
    }

    @Test
    void testValutaSottomissioneNonConsegnata() {
        Sottomissione s = new Sottomissione();
        s.setStato(StatoSottomissione.BOZZA);
        s = sottomissioneRepository.add(s);

        DatiValutazione dv = valutazioneService.creaDTO(70, "Sufficiente");
        Long sId = s.getId();

        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(1L, sId, dv));
    }

    @Test
    void testValutaSottomissioneNonTrovata() {
        DatiValutazione dv = valutazioneService.creaDTO(70, "Sufficiente");
        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(1L, 999L, dv));
    }

    @Test
    void testValutaSottomissioneParametriNull() {
        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(null, 1L, null));
    }
}
