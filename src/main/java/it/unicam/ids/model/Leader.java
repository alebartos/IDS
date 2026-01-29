package it.unicam.ids.model;

import java.util.Objects;

/**
 * Rappresenta un Leader che può creare e gestire un Team.
 * Estende MembroTeam.
 */
public class Leader extends MembroTeam {

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
