package it.unicam.ids.model;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.dto.DatiValutazione;
import java.time.LocalDate;

public class Sottomissione {

    private Long id;
    private DatiProgetto datiProgetto;
    private LocalDate dataInvio;
    private StatoSottomissione stato;
    private Long teamId;
    private Long hackathonId;
    private DatiValutazione datiValutazione;

    public Sottomissione() {
        this.stato = StatoSottomissione.BOZZA;
        this.dataInvio = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DatiProgetto getDatiProgetto() { return datiProgetto; }
    public void setDatiProgetto(DatiProgetto datiProgetto) { this.datiProgetto = datiProgetto; }

    public LocalDate getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDate dataInvio) { this.dataInvio = dataInvio; }

    public StatoSottomissione getStato() { return stato; }
    public void setStato(StatoSottomissione stato) { this.stato = stato; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }

    public DatiValutazione getDatiValutazione() { return datiValutazione; }
    public void setDatiValutazione(DatiValutazione datiValutazione) { this.datiValutazione = datiValutazione; }

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
