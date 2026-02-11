package it.unicam.ids.model;

import java.util.Objects;

/**
 * Rappresenta un Team partecipante agli Hackathon.
 */
public class Team {

    private Long id;
    private String nome;
    private Long leaderId;

    public Team() {
    }

    public Team(Long id, String nome, Long leaderId) {
        this.id = id;
        this.nome = nome;
        this.leaderId = leaderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(nome, team.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", leaderId=" + leaderId +
                '}';
    }
}
