package it.unicam.ids.model;

import jakarta.persistence.*;

@Entity
@Table(name = "organizzatori")
public class Organizzatore extends MembroStaff {

    public Organizzatore() {
    }

    public Organizzatore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    public void creaHackathon() {
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
        return "Organizzatore{" +
               "id=" + getId() +
               ", nome='" + getNome() + '\'' +
               ", cognome='" + getCognome() + '\'' +
               ", email='" + getEmail() + '\'' +
               '}';
    }
}
