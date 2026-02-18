package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "teamId", "destinatario"})
@ToString
public class Invito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataInvio;
    private LocalDate dataRisposta;

    @Enumerated(EnumType.STRING)
    private StatoInvito stato;

    private Long teamId;
    private Long destinatario;

    public Invito() {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.IN_ATTESA;
    }

    public Invito(Long teamId, Long destinatario) {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.IN_ATTESA;
        this.teamId = teamId;
        this.destinatario = destinatario;
    }
}
