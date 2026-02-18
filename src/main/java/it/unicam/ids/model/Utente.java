package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "email"})
@ToString(exclude = "password")
public class Utente {

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
}
