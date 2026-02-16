package it.unicam.ids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {

    private Long id;
    private String nome;
    private Long leaderId;
    private Long viceleaderId;
    private List<Long> membri = new ArrayList<>();

    public Team() {
        this.membri = new ArrayList<>();
    }

    public Team(Long id, String nome, Long leaderId) {
        this.id = id;
        this.nome = nome;
        this.leaderId = leaderId;
        this.membri = new ArrayList<>();
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

    public Long getViceleaderId() {
        return viceleaderId;
    }

    public void setViceleaderId(Long viceleaderId) {
        this.viceleaderId = viceleaderId;
    }

    public List<Long> getMembri() {
        return membri;
    }

    public void setMembri(List<Long> membri) {
        this.membri = membri != null ? new ArrayList<>(membri) : new ArrayList<>();
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
                ", viceleaderId=" + viceleaderId +
                ", membri=" + membri +
                '}';
    }
}
