package it.unicam.ids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un Utente nel sistema.
 * Implementa il pattern CompositeRoleStrategy: un utente può avere più ruoli contemporaneamente.
 */
public class Utente {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private List<Ruolo> ruoli = new ArrayList<>();

    public Utente() {
        this.ruoli.add(Ruolo.BASE);
    }

    public Utente(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruoli = new ArrayList<>();
        this.ruoli.add(Ruolo.BASE);
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ruolo> getRuoli() {
        return new ArrayList<>(ruoli);
    }

    public void setRuoli(List<Ruolo> ruoli) {
        this.ruoli = ruoli != null ? new ArrayList<>(ruoli) : new ArrayList<>();
        if (!this.ruoli.contains(Ruolo.BASE)) {
            this.ruoli.add(Ruolo.BASE);
        }
    }

    /**
     * Aggiunge un ruolo all'utente.
     * @param ruolo il ruolo da aggiungere
     */
    public void addRuolo(Ruolo ruolo) {
        if (ruolo != null && !this.ruoli.contains(ruolo)) {
            this.ruoli.add(ruolo);
        }
    }

    /**
     * Rimuove un ruolo dall'utente.
     * Il ruolo BASE non può essere rimosso.
     * @param ruolo il ruolo da rimuovere
     * @return true se il ruolo è stato rimosso, false altrimenti
     */
    public boolean deleteRuolo(Ruolo ruolo) {
        if (ruolo == Ruolo.BASE) {
            return false;
        }
        return this.ruoli.remove(ruolo);
    }

    /**
     * Verifica se l'utente ha un determinato ruolo.
     * @param ruolo il ruolo da verificare
     * @return true se l'utente ha il ruolo, false altrimenti
     */
    public boolean hasRuolo(Ruolo ruolo) {
        return this.ruoli.contains(ruolo);
    }

    /**
     * Alias per hasRuolo per compatibilità con il diagramma.
     */
    public boolean checkRuolo(Ruolo ruolo) {
        return hasRuolo(ruolo);
    }

    public boolean isOrganizzatore() {
        return hasRuolo(Ruolo.ORGANIZZATORE);
    }

    public boolean isGiudice() {
        return hasRuolo(Ruolo.GIUDICE);
    }

    public boolean isLeader() {
        return hasRuolo(Ruolo.LEADER);
    }

    public boolean isMembroTeam() {
        return hasRuolo(Ruolo.MEMBRO_TEAM);
    }

    public boolean isMembroStaff() {
        return hasRuolo(Ruolo.MEMBRO_STAFF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(id, utente.id) &&
               Objects.equals(email, utente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Utente{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", cognome='" + cognome + '\'' +
               ", email='" + email + '\'' +
               ", ruoli=" + ruoli +
               '}';
    }
}
