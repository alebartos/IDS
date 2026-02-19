package it.unicam.ids.service;

import it.unicam.ids.dto.DatiValutazione;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ValutazioneServiceTest {

    @Autowired
    private ValutazioneService valutazioneService;

    @Autowired
    private SottomissioneRepository sottomissioneRepository;

    @Autowired
    private HackathonRepository hackathonRepository;

    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        hackathon = new Hackathon();
        hackathon.setNome("Hackathon Test");
        hackathon.setStato(StatoHackathon.IN_VALUTAZIONE);
        hackathon = hackathonRepository.save(hackathon);
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
        s.setHackathonId(hackathon.getId());
        s.setStato(StatoSottomissione.CONSEGNATA);
        s = sottomissioneRepository.save(s);

        DatiValutazione dv = valutazioneService.creaDTO(90, "Eccellente");
        Sottomissione valutata = valutazioneService.valutaSottomissione(1L, s.getId(), dv);

        assertEquals(StatoSottomissione.VALUTATA, valutata.getStato());
        assertNotNull(valutata.getDatiValutazione());
        assertEquals(90, valutata.getDatiValutazione().getPunteggio());
    }

    @Test
    void testValutaSottomissioneGiaValutata() {
        Sottomissione s = new Sottomissione();
        s.setHackathonId(hackathon.getId());
        s.setStato(StatoSottomissione.VALUTATA);
        s = sottomissioneRepository.save(s);

        DatiValutazione dv = valutazioneService.creaDTO(80, "Buono");
        Long sId = s.getId();

        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(1L, sId, dv));
    }

    @Test
    void testValutaSottomissioneNonConsegnata() {
        Sottomissione s = new Sottomissione();
        s.setHackathonId(hackathon.getId());
        s.setStato(StatoSottomissione.BOZZA);
        s = sottomissioneRepository.save(s);

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

    @Test
    void testValutaSottomissioneHackathonNonInValutazione() {
        Hackathon hackathonInCorso = new Hackathon();
        hackathonInCorso.setNome("Hackathon In Corso");
        hackathonInCorso.setStato(StatoHackathon.IN_CORSO);
        hackathonInCorso = hackathonRepository.save(hackathonInCorso);

        Sottomissione s = new Sottomissione();
        s.setTeamId(1L);
        s.setHackathonId(hackathonInCorso.getId());
        s.setStato(StatoSottomissione.CONSEGNATA);
        s = sottomissioneRepository.save(s);

        DatiValutazione dv = valutazioneService.creaDTO(90, "Eccellente");
        Long sId = s.getId();

        assertThrows(IllegalArgumentException.class,
                () -> valutazioneService.valutaSottomissione(1L, sId, dv));
    }
}
