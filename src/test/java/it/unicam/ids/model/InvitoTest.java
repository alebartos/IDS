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
        leader.addRuolo(Ruolo.LEADER);

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
        Invito invito = new Invito(team, destinatario);

        assertEquals(team, invito.getTeam());
        assertEquals(destinatario, invito.getDestinatario());
        assertEquals(destinatario.getId(), invito.getDestinatarioId());
        assertEquals(LocalDate.now(), invito.getDataInvio());
        assertEquals(StatoInvito.IN_ATTESA, invito.getStato());
    }

    @Test
    void testAccetta() {
        Invito invito = new Invito(team, destinatario);

        invito.accetta();

        assertEquals(StatoInvito.ACCETTATO, invito.getStato());
        assertEquals(LocalDate.now(), invito.getDataRisposta());
    }

    @Test
    void testRifiuta() {
        Invito invito = new Invito(team, destinatario);

        invito.rifiuta();

        assertEquals(StatoInvito.RIFIUTATO, invito.getStato());
        assertEquals(LocalDate.now(), invito.getDataRisposta());
    }

    @Test
    void testIsInAttesa() {
        Invito invito = new Invito(team, destinatario);

        assertTrue(invito.isInAttesa());

        invito.accetta();
        assertFalse(invito.isInAttesa());
    }

    @Test
    void testSettersGetters() {
        Invito invito = new Invito();
        invito.setId(1L);
        invito.setTeam(team);
        invito.setDestinatario(destinatario);
        invito.setDataInvio(LocalDate.of(2025, 1, 15));
        invito.setDataRisposta(LocalDate.of(2025, 1, 16));
        invito.setStato(StatoInvito.ACCETTATO);

        assertEquals(1L, invito.getId());
        assertEquals(team, invito.getTeam());
        assertEquals(destinatario, invito.getDestinatario());
        assertEquals(destinatario.getId(), invito.getDestinatarioId());
        assertEquals(LocalDate.of(2025, 1, 15), invito.getDataInvio());
        assertEquals(LocalDate.of(2025, 1, 16), invito.getDataRisposta());
        assertEquals(StatoInvito.ACCETTATO, invito.getStato());
    }

    @Test
    void testEquals() {
        Invito invito1 = new Invito(team, destinatario);
        invito1.setId(1L);

        Invito invito2 = new Invito(team, destinatario);
        invito2.setId(1L);

        assertEquals(invito1, invito2);
    }

    @Test
    void testNotEquals() {
        Utente altroDestinatario = new Utente("Paolo", "Verdi", "paolo@example.com", "password");
        altroDestinatario.setId(200L);

        Invito invito1 = new Invito(team, destinatario);
        invito1.setId(1L);

        Invito invito2 = new Invito(team, altroDestinatario);
        invito2.setId(2L);

        assertNotEquals(invito1, invito2);
    }

    @Test
    void testHashCode() {
        Invito invito1 = new Invito(team, destinatario);
        invito1.setId(1L);

        Invito invito2 = new Invito(team, destinatario);
        invito2.setId(1L);

        assertEquals(invito1.hashCode(), invito2.hashCode());
    }

    @Test
    void testToString() {
        Invito invito = new Invito(team, destinatario);
        invito.setId(1L);

        String toString = invito.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("teamId=1"));
        assertTrue(toString.contains("destinatarioId=100"));
        assertTrue(toString.contains("IN_ATTESA"));
    }
}
