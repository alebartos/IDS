package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.Objects;

public class Team {

    private Long id;
    private String nome;
    private String descrizione;
    private LocalDate dataCreazione;
    private Leader leader;

    public Team() {
    }

    public Team(Long id, String nome, String descrizione, LocalDate dataCreazione, Leader leader) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataCreazione = dataCreazione;
        this.leader = leader;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public int getMaxMembriTeam() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(nome, team.nome) &&
                Objects.equals(descrizione, team.descrizione) &&
                Objects.equals(dataCreazione, team.dataCreazione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descrizione, dataCreazione);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}
