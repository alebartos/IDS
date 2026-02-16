package it.unicam.ids.builder;

import it.unicam.ids.model.Hackathon;

import java.time.LocalDate;

public class HackathonBuilder {

    private String nome;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate scadenzaIscrizioni;
    private String luogo;
    private String regolamento;
    private double premio;
    private Integer maxMembriTeam;

    private HackathonBuilder() {
        this.maxMembriTeam = 5;
    }

    public static HackathonBuilder newBuilder() {
        return new HackathonBuilder();
    }

    public HackathonBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public HackathonBuilder descrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public HackathonBuilder dataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    public HackathonBuilder dataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    public HackathonBuilder scadenzaIscrizioni(LocalDate scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        return this;
    }

    public HackathonBuilder luogo(String luogo) {
        this.luogo = luogo;
        return this;
    }

    public HackathonBuilder regolamento(String regolamento) {
        this.regolamento = regolamento;
        return this;
    }

    public HackathonBuilder premio(double premio) {
        this.premio = premio;
        return this;
    }

    public HackathonBuilder maxMembriTeam(Integer maxMembriTeam) {
        this.maxMembriTeam = maxMembriTeam;
        return this;
    }

    public Hackathon build() {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalStateException("Il nome dell'hackathon è obbligatorio");
        }
        if (dataInizio == null) {
            throw new IllegalStateException("La data di inizio è obbligatoria");
        }
        if (dataFine == null) {
            throw new IllegalStateException("La data di fine è obbligatoria");
        }
        Hackathon hackathon = new Hackathon();
        hackathon.setNome(nome);
        hackathon.setDescrizione(descrizione);
        hackathon.setDataInizio(dataInizio);
        hackathon.setDataFine(dataFine);
        hackathon.setScadenzaIscrizioni(scadenzaIscrizioni);
        hackathon.setLuogo(luogo);
        hackathon.setRegolamento(regolamento);
        hackathon.setPremio(premio);
        hackathon.setMaxMembriTeam(maxMembriTeam);
        return hackathon;
    }
}
