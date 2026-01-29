package it.unicam.ids.model;

import java.util.Objects;

/**
 * Rappresenta un membro dello staff di un Hackathon.
 */
public class MembroStaff extends UtenteAstratto {

    private Hackathon hackathon;

    public MembroStaff() {
    }

    public MembroStaff(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MembroStaff that = (MembroStaff) o;
        return Objects.equals(hackathon, that.hackathon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "MembroStaff{" +
               "id=" + getId() +
               ", nome='" + getNome() + '\'' +
               ", cognome='" + getCognome() + '\'' +
               ", email='" + getEmail() + '\'' +
               '}';
    }
}
