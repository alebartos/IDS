package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hackathon {

    private Long id;
    private String nome;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private LocalDate scadenzaIscrizioni;
    private String luogo;
    private String regolamento;
    private double premio;
    private Integer maxMembriTeam;
    private Long organizzatoreId;
    private Long giudiceId;
    private List<Long> membroStaffIds = new ArrayList<>();
    private List<Long> mentoreIds = new ArrayList<>();
    private List<Long> teamIds = new ArrayList<>();
    private Long teamVincitoreId;
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public LocalDate getScadenzaIscrizioni() { return scadenzaIscrizioni; }
    public void setScadenzaIscrizioni(LocalDate scadenzaIscrizioni) { this.scadenzaIscrizioni = scadenzaIscrizioni; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public String getRegolamento() { return regolamento; }
    public void setRegolamento(String regolamento) { this.regolamento = regolamento; }

    public double getPremio() { return premio; }
    public void setPremio(double premio) { this.premio = premio; }

    public int getMaxMembriTeam() { return this.maxMembriTeam; }
    public void setMaxMembriTeam(Integer maxMembriTeam) { this.maxMembriTeam = maxMembriTeam; }

    public Long getOrganizzatoreId() { return organizzatoreId; }
    public void setOrganizzatoreId(Long organizzatoreId) { this.organizzatoreId = organizzatoreId; }

    public Long getGiudiceId() { return giudiceId; }
    public void setGiudiceId(Long giudiceId) { this.giudiceId = giudiceId; }

    public List<Long> getMembroStaffIds() { return membroStaffIds; }
    public void setMembroStaffIds(List<Long> membroStaffIds) { this.membroStaffIds = membroStaffIds; }

    public StatoHackathon getStato() { return stato; }
    public void setStato(StatoHackathon stato) { this.stato = stato; }

    public List<Long> getMentoreIds() { return mentoreIds; }
    public void setMentoreIds(List<Long> mentoreIds) { this.mentoreIds = mentoreIds; }

    public List<Long> getTeamIds() { return teamIds; }
    public void setTeamIds(List<Long> teamIds) { this.teamIds = teamIds; }

    public Long getTeamVincitoreId() { return teamVincitoreId; }
    public void setTeamVincitoreId(Long teamVincitoreId) { this.teamVincitoreId = teamVincitoreId; }

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
