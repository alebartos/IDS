package it.unicam.ids.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DatiValutazioneTest {

    @Test
    void testCostruttoreVuoto() {
        DatiValutazione dv = new DatiValutazione();
        assertEquals(0, dv.getPunteggio());
        assertNull(dv.getGiudizio());
        assertNull(dv.getDataCreazione());
    }

    @Test
    void testCostruttoreConParametri() {
        LocalDate data = LocalDate.now();
        DatiValutazione dv = new DatiValutazione(85, "Ottimo lavoro", data);
        assertEquals(85, dv.getPunteggio());
        assertEquals("Ottimo lavoro", dv.getGiudizio());
        assertEquals(data, dv.getDataCreazione());
    }

    @Test
    void testSetters() {
        DatiValutazione dv = new DatiValutazione();
        LocalDate data = LocalDate.of(2025, 6, 15);
        dv.setPunteggio(90);
        dv.setGiudizio("Eccellente");
        dv.setDataCreazione(data);

        assertEquals(90, dv.getPunteggio());
        assertEquals("Eccellente", dv.getGiudizio());
        assertEquals(data, dv.getDataCreazione());
    }
}
