package it.unicam.ids.model;

import it.unicam.ids.service.Subscriber;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "email"})
@ToString(exclude = "password")
public class Utente implements Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private List<Ruolo> ruoli = new ArrayList<>();

    public Utente() {
        this.ruoli.add(Ruolo.BASE);
    }

    public Utente(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruoli = new ArrayList<>();
        this.ruoli.add(Ruolo.BASE);
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli != null ? new ArrayList<>(ruoli) : new ArrayList<>();
        if (!this.ruoli.contains(Ruolo.BASE)) {
            this.ruoli.add(Ruolo.BASE);
        }
    }

    public void addRuolo(Ruolo r) {
        if (r != null && !this.ruoli.contains(r)) {
            this.ruoli.add(r);
        }
    }

    public void deleteRuolo(Ruolo r) {
        if (r != null && r != Ruolo.BASE) {
            this.ruoli.remove(r);
        }
    }

    public boolean checkRuolo() {
        return this.ruoli != null && !this.ruoli.isEmpty();
    }

    @Override
    public void update(String contesto) {
        System.out.println("[NOTIFICA] Utente " + email + ": " + contesto);
    }
}
