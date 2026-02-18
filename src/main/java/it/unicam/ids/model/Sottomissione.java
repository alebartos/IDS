package it.unicam.ids.model;

import com.ids.dto.DatiProgetto;
import com.ids.dto.DatiValutazione;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString(of = {"id", "datiProgetto", "stato", "teamId", "hackathonId"})
public class Sottomissione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "titolo", column = @Column(name = "progetto_titolo")),
        @AttributeOverride(name = "descrizione", column = @Column(name = "progetto_descrizione")),
        @AttributeOverride(name = "linkRepository", column = @Column(name = "progetto_link_repository"))
    })
    private DatiProgetto datiProgetto;

    private LocalDate dataInvio;

    @Enumerated(EnumType.STRING)
    private StatoSottomissione stato;

    private Long teamId;
    private Long hackathonId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "punteggio", column = @Column(name = "valutazione_punteggio")),
        @AttributeOverride(name = "giudizio", column = @Column(name = "valutazione_giudizio")),
        @AttributeOverride(name = "dataCreazione", column = @Column(name = "valutazione_data_creazione"))
    })
    private DatiValutazione datiValutazione;

    public Sottomissione() {
        this.stato = StatoSottomissione.BOZZA;
        this.dataInvio = LocalDate.now();
    }
}
