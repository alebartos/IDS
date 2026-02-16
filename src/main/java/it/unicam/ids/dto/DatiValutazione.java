package it.unicam.ids.dto;

import java.time.LocalDate;

/**
 * DTO che contiene i dati della valutazione di una sottomissione.
 */
public class DatiValutazione {

    private int punteggio;
    private String giudizio;
    private LocalDate dataCreazione;

    public DatiValutazione() {
    }

    public DatiValutazione(int punteggio, String giudizio, LocalDate dataCreazione) {
        this.punteggio = punteggio;
        this.giudizio = giudizio;
        this.dataCreazione = dataCreazione;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public String getGiudizio() {
        return giudizio;
    }

    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}
