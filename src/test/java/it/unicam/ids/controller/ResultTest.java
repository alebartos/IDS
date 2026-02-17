package it.unicam.ids.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccess() {
        Result<String> result = Result.success("dato");
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertEquals("dato", result.getData());
        assertNull(result.getErrorMessage());
    }

    @Test
    void testCreated() {
        Result<String> result = Result.created("nuovo");
        assertTrue(result.isSuccess());
        assertEquals(201, result.getStatusCode());
        assertEquals("nuovo", result.getData());
        assertNull(result.getErrorMessage());
    }

    @Test
    void testError() {
        Result<String> result = Result.error("errore generico", 500);
        assertFalse(result.isSuccess());
        assertEquals(500, result.getStatusCode());
        assertNull(result.getData());
        assertEquals("errore generico", result.getErrorMessage());
    }

    @Test
    void testBadRequest() {
        Result<String> result = Result.badRequest("parametro non valido");
        assertFalse(result.isSuccess());
        assertEquals(400, result.getStatusCode());
        assertNull(result.getData());
        assertEquals("parametro non valido", result.getErrorMessage());
    }

    @Test
    void testNotFound() {
        Result<String> result = Result.notFound("risorsa non trovata");
        assertFalse(result.isSuccess());
        assertEquals(404, result.getStatusCode());
        assertNull(result.getData());
        assertEquals("risorsa non trovata", result.getErrorMessage());
    }

    @Test
    void testToStringSuccess() {
        Result<String> result = Result.success("dato");
        String str = result.toString();
        assertTrue(str.contains("success=true"));
        assertTrue(str.contains("200"));
    }

    @Test
    void testToStringError() {
        Result<String> result = Result.badRequest("errore");
        String str = result.toString();
        assertTrue(str.contains("success=false"));
        assertTrue(str.contains("400"));
    }
}
