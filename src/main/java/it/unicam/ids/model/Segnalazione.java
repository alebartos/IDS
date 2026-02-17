package it.unicam.ids.model;

import java.util.Objects;

public class Segnalazione {

    private Long id;
    private String descrizione;
    private Long teamId;
    private Long hackathonId;
    private boolean gestita;

    public Segnalazione() {
        this.gestita = false;
    }

    public Segnalazione(String descrizione, Long teamId, Long hackathonId) {
        this.descrizione = descrizione;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.gestita = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }

    public boolean isGestita() { return gestita; }
    public void setGestita(boolean gestita) { this.gestita = gestita; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segnalazione that = (Segnalazione) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Segnalazione{" +
               "id=" + id +
               ", descrizione='" + descrizione + '\'' +
               ", teamId=" + teamId +
               ", hackathonId=" + hackathonId +
               ", gestita=" + gestita +
               '}';
    }
}
