package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvitoTest {

    private Team team;
    private Utente leader;
    private Utente destinatario;

    @BeforeEach
    void setUp() {
        leader = new Utente("Mario", "Rossi", "mario@example.com", "password");
        leader.setId(1L);
        leader.getRuoli().add(Ruolo.LEADER);

        team = new Team();
        team.setId(1L);
        team.setNome("Team Test");
        team.setLeaderId(leader.getId());

        destinatario = new Utente("Anna", "Bianchi", "anna@example.com", "password");
        destinatario.setId(100L);
    }

    @Test
    void testCostruttoreDefault() {
        Invito invito = new Invito();

        assertNotNull(invito.getDataInvio());
        assertEquals(LocalDate.now(), invito.getDataInvio());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testCostruttoreConParametri() {
        Invito invito = new Invito(team.getId(), destinatario.getId());

        assertEquals(team.getId(), invito.getTeamId());
        assertEquals(destinatario.getId(), invito.getDestinatario());
        assertEquals(LocalDate.now(), invito.getDataInvio());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testAccetta() {
        Invito invito = new Invito(team.getId(), destinatario.getId());

        invito.setStato(StatoInvito.ACCETTATO);
        invito.setDataRisposta(LocalDate.now());

        assertEquals(StatoInvito.ACCETTATO, invito.getStato());
        assertEquals(LocalDate.now(), invito.getDataRisposta());
    }

    @Test
    void testRifiuta() {
        Invito invito = new Invito(team.getId(), destinatario.getId());

        invito.setStato(StatoInvito.RIFIUTATO);
        invito.setDataRisposta(LocalDate.now());

        assertEquals(StatoInvito.RIFIUTATO, invito.getStato());
        assertEquals(LocalDate.now(), invito.getDataRisposta());
    }

    @Test
    void testIsInAttesa() {
        Invito invito = new Invito(team.getId(), destinatario.getId());

        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());

        invito.setStato(StatoInvito.ACCETTATO);
        assertNotEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testSettersGetters() {
        Invito invito = new Invito();
        invito.setId(1L);
        invito.setTeamId(team.getId());
        invito.setDestinatario(destinatario.getId());
        invito.setDataInvio(LocalDate.of(2025, 1, 15));
        invito.setDataRisposta(LocalDate.of(2025, 1, 16));
        invito.setStato(StatoInvito.ACCETTATO);

        assertEquals(1L, invito.getId());
        assertEquals(team.getId(), invito.getTeamId());
        assertEquals(destinatario.getId(), invito.getDestinatario());
        assertEquals(LocalDate.of(2025, 1, 15), invito.getDataInvio());
        assertEquals(LocalDate.of(2025, 1, 16), invito.getDataRisposta());
        assertEquals(StatoInvito.ACCETTATO, invito.getStato());
    }

    @Test
    void testEquals() {
        Invito invito1 = new Invito(team.getId(), destinatario.getId());
        invito1.setId(1L);

        Invito invito2 = new Invito(team.getId(), destinatario.getId());
        invito2.setId(1L);

        assertEquals(invito1, invito2);
    }

    @Test
    void testNotEquals() {
        Utente altroDestinatario = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        altroDestinatario.setId(200L);

        Invito invito1 = new Invito(team.getId(), destinatario.getId());
        invito1.setId(1L);

        Invito invito2 = new Invito(team.getId(), altroDestinatario.getId());
        invito2.setId(2L);

        assertNotEquals(invito1, invito2);
    }

    @Test
    void testHashCode() {
        Invito invito1 = new Invito(team.getId(), destinatario.getId());
        invito1.setId(1L);

        Invito invito2 = new Invito(team.getId(), destinatario.getId());
        invito2.setId(1L);

        assertEquals(invito1.hashCode(), invito2.hashCode());
    }

    @Test
    void testToString() {
        Invito invito = new Invito(team.getId(), destinatario.getId());
        invito.setId(1L);

        String toString = invito.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("teamId=1"));
        assertTrue(toString.contains("destinatario=100"));
        assertTrue(toString.contains("IN_ATTESA"));
    }
}
