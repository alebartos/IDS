package it.unicam.ids.model;

import it.unicam.ids.dto.DatiProgetto;
import java.time.LocalDate;

/**
 * Rappresenta una sottomissione di un progetto da parte di un team per un hackathon.
 */
public class Sottomissione {

    private Long id;
    private DatiProgetto datiProgetto;
    private LocalDate dataInvio;
    private StatoSottomissione stato;
    private Long teamId;
    private Long hackathonId;

    public Sottomissione() {
        this.stato = StatoSottomissione.BOZZA;
        this.dataInvio = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DatiProgetto getDatiProgetto() {
        return datiProgetto;
    }

    public void setDatiProgetto(DatiProgetto datiProgetto) {
        this.datiProgetto = datiProgetto;
        this.dataInvio = LocalDate.now();
    }

    public LocalDate getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    public StatoSottomissione getStato() {
        return stato;
    }

    public void setStato(StatoSottomissione stato) {
        this.stato = stato;
        this.dataInvio = LocalDate.now();
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
                ", datiProgetto=" + datiProgetto +
                ", stato=" + stato +
                ", teamId=" + teamId +
                ", hackathonId=" + hackathonId +
                '}';
    }
}
