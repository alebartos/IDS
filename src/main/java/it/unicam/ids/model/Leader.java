package it.unicam.ids.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "leader")
public class Leader extends UtenteAstratto {

    @OneToOne(mappedBy = "leader", cascade = CascadeType.ALL)
    private Team team;

    public Leader() {
    }

    public Leader(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void creaTeam() {
    }

    public void iscriviTeam() {
    }

    public void selezionaPartecipanti() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Leader leader = (Leader) o;
        return Objects.equals(team, leader.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "Leader{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
