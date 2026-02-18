package it.unicam.ids.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "nome", "dataInizio", "dataFine"})
@ToString(of = {"id", "nome", "descrizione", "dataInizio", "dataFine", "luogo", "maxMembriTeam"})
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate scadenzaIscrizioni;
    private String luogo;
    private String regolamento;
    private double premio;

    @Getter(AccessLevel.NONE)
    private Integer maxMembriTeam;

    private Long organizzatoreId;
    private Long giudiceId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hackathon_staff")
    private List<Long> membroStaffIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hackathon_mentori")
    private List<Long> mentoreIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hackathon_teams")
    private List<Long> teamIds = new ArrayList<>();

    private Long teamVincitoreId;

    @Enumerated(EnumType.STRING)
    private StatoHackathon stato;

    public Hackathon() {
        this.stato = StatoHackathon.IN_ISCRIZIONE;
        this.membroStaffIds = new ArrayList<>();
        this.mentoreIds = new ArrayList<>();
        this.teamIds = new ArrayList<>();
    }

    public Hackathon(Long id, String nome, String descrizione, LocalDate dataInizio, LocalDate dataFine,
                     LocalDate scadenzaIscrizioni, String luogo, String regolamento, double premio,
                     Integer maxMembriTeam) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.luogo = luogo;
        this.regolamento = regolamento;
        this.premio = premio;
        this.maxMembriTeam = maxMembriTeam;
        this.membroStaffIds = new ArrayList<>();
        this.mentoreIds = new ArrayList<>();
        this.teamIds = new ArrayList<>();
    }

    public int getMaxMembriTeam() { return this.maxMembriTeam; }
}
