package it.unicam.ids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un Team partecipante agli Hackathon.
 */
public class Team {

    private Long id;
    private String nome;
    private Long leaderId;
    private List<Long> membri = new ArrayList<>();

    public Team() {
        this.membri = new ArrayList<>();
    }

    public Team(Long id, String nome, Long leaderId) {
        this.id = id;
        this.nome = nome;
        this.leaderId = leaderId;
        this.membri = new ArrayList<>();
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

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public List<Long> getMembri() {
        return new ArrayList<>(membri);
    }

    public void setMembri(List<Long> membri) {
        this.membri = membri != null ? new ArrayList<>(membri) : new ArrayList<>();
    }

    /**
     * Aggiunge un membro al team.
     * @param utenteId l'ID dell'utente da aggiungere
     */
    public void addMembro(Long utenteId) {
        if (utenteId != null && !this.membri.contains(utenteId)) {
            this.membri.add(utenteId);
        }
    }

    /**
     * Rimuove un membro dal team.
     * @param membroId l'ID del membro da rimuovere
     * @return true se il membro è stato rimosso, false altrimenti
     */
    public boolean rimuoviMembro(Long membroId) {
        return this.membri.remove(membroId);
    }

    /**
     * Verifica se un utente è membro del team.
     * @param membroId l'ID del membro da cercare
     * @return true se l'utente è membro del team, false altrimenti
     */
    public boolean findById(Long membroId) {
        return this.membri.contains(membroId);
    }

    /**
     * Restituisce il numero di membri del team.
     * @return il numero di membri
     */
    public int getSizeTeam() {
        return this.membri.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) &&
                Objects.equals(nome, team.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", leaderId=" + leaderId +
                ", membri=" + membri +
                '}';
    }
}
