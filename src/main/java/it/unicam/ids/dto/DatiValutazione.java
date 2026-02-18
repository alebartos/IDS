package it.unicam.ids.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatiValutazione {

    private int punteggio;
    private String giudizio;
    private LocalDate dataCreazione;
}
