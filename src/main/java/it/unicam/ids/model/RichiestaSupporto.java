package it.unicam.ids.model;

import java.time.LocalDate;
import java.util.Objects;

public class RichiestaSupporto {

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }

    public boolean isRisolta() { return risolta; }
    public void setRisolta(boolean risolta) { this.risolta = risolta; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RichiestaSupporto that = (RichiestaSupporto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RichiestaSupporto{" +
                "id=" + id +
                ", descrizione='" + descrizione + '\'' +
                ", teamId=" + teamId +
                ", data=" + data +
                ", hackathonId=" + hackathonId +
                ", risolta=" + risolta +
                '}';
    }
}
