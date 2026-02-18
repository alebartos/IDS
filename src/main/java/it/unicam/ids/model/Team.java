package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "nome"})
@ToString
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Long leaderId;
    private Long viceleaderId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private List<Long> membri = new ArrayList<>();

    public Team() { this.membri = new ArrayList<>(); }

    public Team(Long id, String nome, Long leaderId) {
        this.id = id;
        this.nome = nome;
        this.leaderId = leaderId;
        this.membri = new ArrayList<>();
    }

    public void setMembri(List<Long> membri) {
        this.membri = membri != null ? new ArrayList<>(membri) : new ArrayList<>();
    }
}
