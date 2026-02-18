package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Segnalazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descrizione;
    private Long teamId;
    private Long hackathonId;
    private boolean gestita;

    public Segnalazione() { this.gestita = false; }

    public Segnalazione(String descrizione, Long teamId, Long hackathonId) {
        this.descrizione = descrizione;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.gestita = false;
    }
}
