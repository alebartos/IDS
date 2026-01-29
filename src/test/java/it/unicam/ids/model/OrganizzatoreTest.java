package it.unicam.ids.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizzatoreTest {

    private Organizzatore organizzatore;

    @BeforeEach
    void setUp() {
        organizzatore = new Organizzatore("Luigi", "Verdi", "luigi.verdi@example.com", "password456");
    }

    @Test
    void testOrganizzatoreCreation() {
        assertNotNull(organizzatore);
        assertEquals("Luigi", organizzatore.getNome());
        assertEquals("Verdi", organizzatore.getCognome());
        assertEquals("luigi.verdi@example.com", organizzatore.getEmail());
        assertEquals("password456", organizzatore.getPassword());
    }
}
