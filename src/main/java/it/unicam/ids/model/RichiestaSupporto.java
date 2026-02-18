package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class RichiestaSupporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descrizione;
    private Long teamId;
    private LocalDate data;
    private Long hackathonId;
    private boolean risolta;

    public RichiestaSupporto() {
        this.data = LocalDate.now();
        this.risolta = false;
    }

    public RichiestaSupporto(String descrizione, Long teamId, Long hackathonId) {
        this.descrizione = descrizione;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.data = LocalDate.now();
        this.risolta = false;
    }
}
