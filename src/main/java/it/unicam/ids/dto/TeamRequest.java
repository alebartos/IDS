package it.unicam.ids.dto;

import java.util.Objects;

/**
 * DTO per la richiesta di creazione di un Team.
 */
public class TeamRequest {

    private String nome;
    private String descrizione;
    private Long leaderId;

    public TeamRequest() {
    }

    public TeamRequest(String nome, String descrizione, Long leaderId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.leaderId = leaderId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamRequest that = (TeamRequest) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(descrizione, that.descrizione) &&
                Objects.equals(leaderId, that.leaderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, descrizione, leaderId);
    }

    @Override
    public String toString() {
        return "TeamRequest{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", leaderId=" + leaderId +
                '}';
    }
}
