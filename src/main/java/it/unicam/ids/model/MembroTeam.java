package it.unicam.ids.model;

/**
 * Rappresenta un membro di un Team.
 */
public class MembroTeam extends UtenteAstratto {

    public MembroTeam() {
    }

    public MembroTeam(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "MembroTeam{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", cognome='" + getCognome() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
