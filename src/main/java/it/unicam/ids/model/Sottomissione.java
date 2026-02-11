package it.unicam.ids.model;

import java.time.LocalDate;

/**
 * Rappresenta una sottomissione di un progetto da parte di un team per un hackathon.
 */
public class Sottomissione {

    private Long id;
    private String descrizione;
    private String linkRepository;
    private String fileAllegati;
    private LocalDate dataUltimaModifica;
    private StatoSottomissione stato;
    private Long teamId;
    private Long hackathonId;

    public Sottomissione() {
        this.stato = StatoSottomissione.BOZZA;
        this.dataUltimaModifica = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
        this.dataUltimaModifica = LocalDate.now();
    }

    public String getLinkRepository() {
        return linkRepository;
    }

    public void setLinkRepository(String linkRepository) {
        this.linkRepository = linkRepository;
        this.dataUltimaModifica = LocalDate.now();
    }

    public String getFileAllegati() {
        return fileAllegati;
    }

    public void setFileAllegati(String fileAllegati) {
        this.fileAllegati = fileAllegati;
        this.dataUltimaModifica = LocalDate.now();
    }

    public LocalDate getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(LocalDate dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public StatoSottomissione getStato() {
        return stato;
    }

    public void setStato(StatoSottomissione stato) {
        this.stato = stato;
        this.dataUltimaModifica = LocalDate.now();
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public void setHackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
    }

    /**
     * Verifica se la sottomissione è in stato bozza.
     */
    public boolean isBozza() {
        return this.stato == StatoSottomissione.BOZZA;
    }

    /**
     * Verifica se la sottomissione è stata consegnata.
     */
    public boolean isConsegnata() {
        return this.stato == StatoSottomissione.CONSEGNATA;
    }

    @Override
    public String toString() {
        return "Sottomissione{" +
                "id=" + id +
                ", descrizione='" + descrizione + '\'' +
                ", stato=" + stato +
                ", teamId=" + teamId +
                ", hackathonId=" + hackathonId +
                '}';
    }
}
