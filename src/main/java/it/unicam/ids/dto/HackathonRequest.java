package it.unicam.ids.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO per la richiesta di creazione di un Hackathon.
 */
public class HackathonRequest {

    private String nome;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate scadenzaIscrizioni;
    private String luogo;
    private String regolamento;
    private double premio;
    private Integer maxMembriTeam;

    public HackathonRequest() {
    }

    public HackathonRequest(String nome, String descrizione, LocalDate dataInizio, LocalDate dataFine,
                            LocalDate scadenzaIscrizioni, String luogo, String regolamento,
                            double premio, Integer maxMembriTeam) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.luogo = luogo;
        this.regolamento = regolamento;
        this.premio = premio;
        this.maxMembriTeam = maxMembriTeam;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public LocalDate getScadenzaIscrizioni() {
        return scadenzaIscrizioni;
    }

    public void setScadenzaIscrizioni(LocalDate scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getRegolamento() {
        return regolamento;
    }

    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public double getPremio() {
        return premio;
    }

    public void setPremio(double premio) {
        this.premio = premio;
    }

    public Integer getMaxMembriTeam() {
        return maxMembriTeam;
    }

    public void setMaxMembriTeam(Integer maxMembriTeam) {
        this.maxMembriTeam = maxMembriTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HackathonRequest that = (HackathonRequest) o;
        return Objects.equals(nome, that.nome) &&
                Objects.equals(dataInizio, that.dataInizio) &&
                Objects.equals(dataFine, that.dataFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, dataInizio, dataFine);
    }

    @Override
    public String toString() {
        return "HackathonRequest{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", luogo='" + luogo + '\'' +
                '}';
    }
}
