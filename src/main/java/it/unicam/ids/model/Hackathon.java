package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un Hackathon.
 */
public class Hackathon {

    private Long id;
    private String nome;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate scadenzaIscrizioni;
    private String luogo;
    private String regolamento;
    private String premio;
    private Integer maxMembriTeam;
    private List<Utente> membriStaff = new ArrayList<>();
    private Utente giudice;

    public Hackathon() {
    }

    public Hackathon(Long id, String nome, String descrizione, LocalDate dataInizio, LocalDate dataFine,
                     LocalDate scadenzaIscrizioni, String luogo, String regolamento, String premio,
                     Integer maxMembriTeam, List<Utente> membriStaff) {
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
        this.membriStaff = membriStaff != null ? membriStaff : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public int getMaxMembriTeam() {
        return this.maxMembriTeam;
    }

    public void setMaxMembriTeam(Integer maxMembriTeam) {
        this.maxMembriTeam = maxMembriTeam;
    }

    public List<Utente> getMembriStaff() {
        return membriStaff;
    }

    public void setMembriStaff(List<Utente> membriStaff) {
        this.membriStaff = membriStaff;
    }

    public void addMembroStaff(Utente membro) {
        if (membro != null && !this.membriStaff.contains(membro)) {
            this.membriStaff.add(membro);
        }
    }

    public boolean removeMembroStaff(Utente membro) {
        return this.membriStaff.remove(membro);
    }

    public Utente getGiudice() {
        return giudice;
    }

    public void setGiudice(Utente giudice) {
        this.giudice = giudice;
    }

    public boolean hasGiudice() {
        return this.giudice != null;
    }

    /**
     * Verifica se un utente è già assegnato come staff a questo hackathon.
     */
    public boolean checkStaff(Utente utente) {
        return this.membriStaff.contains(utente);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hackathon hackathon = (Hackathon) o;
        return Objects.equals(id, hackathon.id) &&
                Objects.equals(nome, hackathon.nome) &&
                Objects.equals(dataInizio, hackathon.dataInizio) &&
                Objects.equals(dataFine, hackathon.dataFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataInizio, dataFine);
    }

    @Override
    public String toString() {
        return "Hackathon{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", dataInizio=" + dataInizio +
                ", dataFine=" + dataFine +
                ", luogo='" + luogo + '\'' +
                ", maxMembriTeam=" + maxMembriTeam +
                '}';
    }
}
