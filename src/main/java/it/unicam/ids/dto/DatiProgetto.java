package it.unicam.ids.dto;

/**
 * DTO per i dati di un progetto da sottomettere.
 */
public class DatiProgetto {

    private String titolo;
    private String descrizione;
    private String linkRepository;

    public DatiProgetto() {
    }

    public DatiProgetto(String titolo, String descrizione, String linkRepository) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.linkRepository = linkRepository;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLinkRepository() {
        return linkRepository;
    }

    public void setLinkRepository(String linkRepository) {
        this.linkRepository = linkRepository;
    }
}
