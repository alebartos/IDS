package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        hackathon.setPremio(5000.0);
        hackathon.setMaxMembriTeam(5);
    }

    @Test
    void testHackathonCreation() {
        assertNotNull(hackathon);
        assertEquals("Hackathon 2025", hackathon.getNome());
        assertEquals("Evento di programmazione", hackathon.getDescrizione());
        assertEquals("Milano", hackathon.getLuogo());
        assertEquals(5000.0, hackathon.getPremio());
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
    void testOrganizzatoreId() {
        assertNull(hackathon.getOrganizzatoreId());

        hackathon.setOrganizzatoreId(1L);

        assertEquals(1L, hackathon.getOrganizzatoreId());
    }

    @Test
    void testGiudiceId() {
        assertNull(hackathon.getGiudiceId());

        hackathon.setGiudiceId(2L);

        assertEquals(2L, hackathon.getGiudiceId());
        assertNotNull(hackathon.getGiudiceId());
    }

    @Test
    void testMembroStaffIdsListInitialization() {
        assertNotNull(hackathon.getMembroStaffIds());
        assertTrue(hackathon.getMembroStaffIds().isEmpty());
    }

    @Test
    void testAddMembroStaffId() {
        Long staffId = 1L;

        hackathon.getMembroStaffIds().add(staffId);

        assertEquals(1, hackathon.getMembroStaffIds().size());
        assertTrue(hackathon.getMembroStaffIds().contains(staffId));
    }

    @Test
    void testCheckStaff() {
        Long staffId = 1L;

        assertFalse(hackathon.getMembroStaffIds().contains(staffId));

        hackathon.getMembroStaffIds().add(staffId);

        assertTrue(hackathon.getMembroStaffIds().contains(staffId));
    }

    @Test
    void testRemoveMembroStaffId() {
        Long staffId = 1L;

        hackathon.getMembroStaffIds().add(staffId);
        assertTrue(hackathon.getMembroStaffIds().contains(staffId));

        boolean removed = hackathon.getMembroStaffIds().remove(staffId);

        assertTrue(removed);
        assertFalse(hackathon.getMembroStaffIds().contains(staffId));
    }

    @Test
    void testMentoreIdsListInitialization() {
        assertNotNull(hackathon.getMentoreIds());
        assertTrue(hackathon.getMentoreIds().isEmpty());
    }

    @Test
    void testAddMentoreId() {
        Long mentoreId = 1L;

        hackathon.getMentoreIds().add(mentoreId);

        assertEquals(1, hackathon.getMentoreIds().size());
        assertTrue(hackathon.getMentoreIds().contains(mentoreId));
    }

    @Test
    void testCheckMentoreEsistente() {
        Long mentoreId = 1L;

        assertFalse(hackathon.getMentoreIds().contains(mentoreId));

        hackathon.getMentoreIds().add(mentoreId);

        assertTrue(hackathon.getMentoreIds().contains(mentoreId));
    }

    @Test
    void testRemoveMentoreId() {
        Long mentoreId = 1L;

        hackathon.getMentoreIds().add(mentoreId);
        assertTrue(hackathon.getMentoreIds().contains(mentoreId));

        boolean removed = hackathon.getMentoreIds().remove(mentoreId);

        assertTrue(removed);
        assertFalse(hackathon.getMentoreIds().contains(mentoreId));
    }

    @Test
    void testTeamIdsListInitialization() {
        assertNotNull(hackathon.getTeamIds());
        assertTrue(hackathon.getTeamIds().isEmpty());
    }

    @Test
    void testAddTeamId() {
        Long teamId = 1L;

        hackathon.getTeamIds().add(teamId);

        assertEquals(1, hackathon.getTeamIds().size());
        assertTrue(hackathon.getTeamIds().contains(teamId));
    }

    @Test
    void testRemoveTeamId() {
        Long teamId = 1L;

        hackathon.getTeamIds().add(teamId);
        assertTrue(hackathon.getTeamIds().contains(teamId));

        boolean removed = hackathon.getTeamIds().remove(teamId);

        assertTrue(removed);
        assertFalse(hackathon.getTeamIds().contains(teamId));
    }

    @Test
    void testTeamVincitoreId() {
        assertNull(hackathon.getTeamVincitoreId());

        hackathon.setTeamVincitoreId(5L);

        assertEquals(5L, hackathon.getTeamVincitoreId());
    }
}
