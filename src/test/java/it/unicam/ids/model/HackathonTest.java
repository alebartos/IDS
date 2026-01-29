package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HackathonTest {

    private Hackathon hackathon;

    @BeforeEach
    void setUp() {
        hackathon = new Hackathon();
        hackathon.setNome("Hackathon 2025");
        hackathon.setDescrizione("Evento di programmazione");
        hackathon.setDataInizio(LocalDate.of(2025, 3, 1));
        hackathon.setDataFine(LocalDate.of(2025, 3, 3));
        hackathon.setScadenzaIscrizioni(LocalDate.of(2025, 2, 15));
        hackathon.setLuogo("Milano");
        hackathon.setRegolamento("Regole di partecipazione");
        hackathon.setPremio("5000 Euro");
        hackathon.setMaxMembriTeam(5);
        hackathon.setMembriStaff(new ArrayList<>());
    }

    @Test
    void testHackathonCreation() {
        assertNotNull(hackathon);
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals("Evento di programmazione", hackathon.getDescrizione());
        assertEquals("Milano", hackathon.getLuogo());
        assertEquals("5000 Euro", hackathon.getPremio());
        assertEquals(5, hackathon.getMaxMembriTeam());
    }

    @Test
    void testHackathonDates() {
        assertEquals(LocalDate.of(2025, 3, 1), hackathon.getDataInizio());
        assertEquals(LocalDate.of(2025, 3, 3), hackathon.getDataFine());
        assertEquals(LocalDate.of(2025, 2, 15), hackathon.getScadenzaIscrizioni());

        assertTrue(hackathon.getScadenzaIscrizioni().isBefore(hackathon.getDataInizio()));
        assertTrue(hackathon.getDataInizio().isBefore(hackathon.getDataFine()));
    }

    @Test
    void testGetMaxMembriTeam() {
        assertEquals(5, hackathon.getMaxMembriTeam());
    }

    @Test
    void testMembriStaffList() {
        assertNotNull(hackathon.getMembriStaff());
        assertTrue(hackathon.getMembriStaff().isEmpty());
    }
}
