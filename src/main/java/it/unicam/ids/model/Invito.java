package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.Objects;

public class Invito {

    private Long id;
    private LocalDate dataInvio;
    private LocalDate dataRisposta;
    private StatoInvito stato;
    private Team team;
    private Utente destinatario;

    public Invito() {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.IN_ATTESA;
    }

    public Invito(Team team, Utente destinatario) {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.IN_ATTESA;
        this.team = team;
        this.destinatario = destinatario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDate dataInvio) { this.dataInvio = dataInvio; }

    public LocalDate getDataRisposta() { return dataRisposta; }
    public void setDataRisposta(LocalDate dataRisposta) { this.dataRisposta = dataRisposta; }

    public StatoInvito getStato() { return stato; }
    public void setStato(StatoInvito stato) { this.stato = stato; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public Utente getDestinatario() { return destinatario; }
    public void setDestinatario(Utente destinatario) { this.destinatario = destinatario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invito invito = (Invito) o;
        return Objects.equals(id, invito.id) &&
                Objects.equals(team, invito.team) &&
                Objects.equals(destinatario, invito.destinatario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, team, destinatario);
    }

    @Override
    public String toString() {
        return "Invito{" +
                "id=" + id +
                ", dataInvio=" + dataInvio +
                ", dataRisposta=" + dataRisposta +
                ", stato=" + stato +
                ", teamId=" + (team != null ? team.getId() : null) +
                ", destinatarioId=" + (destinatario != null ? destinatario.getId() : null) +
                '}';
    }
}
