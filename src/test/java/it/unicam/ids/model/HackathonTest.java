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
        assertFalse(hackathon.hasGiudice());

        hackathon.setGiudiceId(2L);

        assertEquals(2L, hackathon.getGiudiceId());
        assertTrue(hackathon.hasGiudice());
    }

    @Test
    void testMembroStaffIdsListInitialization() {
        assertNotNull(hackathon.getMembroStaffIds());
        assertTrue(hackathon.getMembroStaffIds().isEmpty());
    }

    @Test
    void testAddMembroStaffId() {
        Long staffId = 1L;

        hackathon.addMembroStaffId(staffId);

        assertEquals(1, hackathon.getMembroStaffIds().size());
        assertTrue(hackathon.getMembroStaffIds().contains(staffId));
    }

    @Test
    void testAddMembroStaffIdDuplicato() {
        Long staffId = 1L;

        hackathon.addMembroStaffId(staffId);
        hackathon.addMembroStaffId(staffId);

        assertEquals(1, hackathon.getMembroStaffIds().size());
    }

    @Test
    void testCheckStaff() {
        Long staffId = 1L;

        assertFalse(hackathon.checkStaff(staffId));

        hackathon.addMembroStaffId(staffId);

        assertTrue(hackathon.checkStaff(staffId));
    }

    @Test
    void testRemoveMembroStaffId() {
        Long staffId = 1L;

        hackathon.addMembroStaffId(staffId);
        assertTrue(hackathon.checkStaff(staffId));

        boolean removed = hackathon.removeMembroStaffId(staffId);

        assertTrue(removed);
        assertFalse(hackathon.checkStaff(staffId));
    }

    @Test
    void testMentoreIdsListInitialization() {
        assertNotNull(hackathon.getMentoreIds());
        assertTrue(hackathon.getMentoreIds().isEmpty());
    }

    @Test
    void testAddMentoreId() {
        Long mentoreId = 1L;

        hackathon.addMentoreId(mentoreId);

        assertEquals(1, hackathon.getMentoreIds().size());
        assertTrue(hackathon.getMentoreIds().contains(mentoreId));
    }

    @Test
    void testAddMentoreIdDuplicato() {
        Long mentoreId = 1L;

        hackathon.addMentoreId(mentoreId);
        hackathon.addMentoreId(mentoreId);

        assertEquals(1, hackathon.getMentoreIds().size());
    }

    @Test
    void testAddMentoreIdNull() {
        hackathon.addMentoreId(null);

        assertTrue(hackathon.getMentoreIds().isEmpty());
    }

    @Test
    void testCheckMentoreEsistente() {
        Long mentoreId = 1L;

        assertFalse(hackathon.checkMentoreEsistente(mentoreId));

        hackathon.addMentoreId(mentoreId);

        assertTrue(hackathon.checkMentoreEsistente(mentoreId));
    }

    @Test
    void testRemoveMentoreId() {
        Long mentoreId = 1L;

        hackathon.addMentoreId(mentoreId);
        assertTrue(hackathon.checkMentoreEsistente(mentoreId));

        boolean removed = hackathon.removeMentoreId(mentoreId);

        assertTrue(removed);
        assertFalse(hackathon.checkMentoreEsistente(mentoreId));
    }

    @Test
    void testTeamIdsListInitialization() {
        assertNotNull(hackathon.getTeamIds());
        assertTrue(hackathon.getTeamIds().isEmpty());
    }

    @Test
    void testAddTeamId() {
        Long teamId = 1L;

        hackathon.addTeamId(teamId);

        assertEquals(1, hackathon.getTeamIds().size());
        assertTrue(hackathon.getTeamIds().contains(teamId));
    }

    @Test
    void testRemoveTeamId() {
        Long teamId = 1L;

        hackathon.addTeamId(teamId);
        assertTrue(hackathon.getTeamIds().contains(teamId));

        boolean removed = hackathon.removeTeamId(teamId);

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
