package it.unicam.ids.model;

import jakarta.persistence.*;

@Entity
@Table
public class MembroStaff extends UtenteAstratto {
    @ManyToOne
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    //da finire
}