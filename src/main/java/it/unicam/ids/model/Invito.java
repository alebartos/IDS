package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Rappresenta un invito a far parte di un Team.
 */
public class Invito {

    private Long id;
    private LocalDate dataInvio;
    private StatoInvito stato;
    private Team team;
    private Utente destinatario;

    public Invito() {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.PENDING;
    }

    public Invito(Team team, Utente destinatario) {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.PENDING;
        this.team = team;
        this.destinatario = destinatario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
    }

    public StatoInvito getStato() {
        return stato;
    }

    public void setStato(StatoInvito stato) {
        this.stato = stato;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Utente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Utente destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Metodo di convenienza per ottenere l'ID del destinatario.
     */
    public Long getDestinatarioId() {
        return destinatario != null ? destinatario.getId() : null;
    }

    public void accetta() {
        this.stato = StatoInvito.ACCEPTED;
    }

    public void rifiuta() {
        this.stato = StatoInvito.REJECTED;
    }

    public boolean isPending() {
        return this.stato == StatoInvito.PENDING;
    }

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
               ", stato=" + stato +
               ", teamId=" + (team != null ? team.getId() : null) +
               ", destinatarioId=" + (destinatario != null ? destinatario.getId() : null) +
               '}';
    }
}
