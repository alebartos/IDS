package it.unicam.ids.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatiProgetto {

    private String titolo;
    private String descrizione;
    private String linkRepository;
}
