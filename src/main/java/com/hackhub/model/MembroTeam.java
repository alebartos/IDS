package com.hackhub.model;

import java.time.LocalDate;

/**
 * Classe che rappresenta un membro di un team in HackHub.
 * <p>
 * Un MembroTeam è un Utente che fa parte di un team. Puo':
 * - Abbandonare il team
 * - Essere nominato Viceleader dal Leader
 * <p>
 * Estende: Utente
 * Esteso da: Leader
 */
public class MembroTeam extends Utente {

    /** Indica se il membro è il Viceleader del team */
    private boolean isViceLeader;

    /** Data in cui il membro è entrato nel team */
    private LocalDate dataIngresso;

    /**
     * Costruttore della classe MembroTeam.
     *
     * @param nome     Il nome del membro
     * @param cognome  Il cognome del membro
     * @param email    L'email del membro
     * @param password La password del membro
     */
    public MembroTeam(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.isViceLeader = false;
        this.dataIngresso = LocalDate.now();
    }

    // ==================== GETTER ====================

    /**
     * Verifica se il membro è il Viceleader del team.
     *
     * @return true se è Viceleader, false altrimenti
     */
    public boolean isViceLeader() {
        return isViceLeader;
    }

    /**
     * Restituisce la data di ingresso nel team.
     *
     * @return La data di ingresso
     */
    public LocalDate getDataIngresso() {
        return dataIngresso;
    }

    // ==================== SETTER ====================

    /**
     * Imposta lo stato di Viceleader del membro.
     *
     * @param isViceLeader true per nominare Viceleader, false per revocare
     */
    public void setViceLeader(boolean isViceLeader) {
        this.isViceLeader = isViceLeader;
    }

    /**
     * Imposta la data di ingresso nel team.
     *
     * @param dataIngresso La data di ingresso
     */
    public void setDataIngresso(LocalDate dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Abbandona il team corrente.
     * <p>
     * La logica varia in base al ruolo:
     * - Se è Leader con Viceleader: il Viceleader diventa Leader
     * - Se è Leader senza Viceleader e unico membro: il team viene eliminato
     * - Se è Leader senza Viceleader ma con altri membri: deve prima nominare un Viceleader
     * - Se è Viceleader: perde il ruolo e lascia il team
     * - Se è membro normale: lascia semplicemente il team
     * <p>
     * Precondizioni:
     * - Il membro deve appartenere a un team
     * <p>
     * Postcondizioni:
     * - Il membro non appartiene più al team
     * - Se era Viceleader, il ruolo viene revocato
     * - Se era Leader, la gestione viene delegata alla classe Leader
     *
     * @throws IllegalStateException se il membro non appartiene a nessun team
     */
    public void abbandonaTeam() {
        Team teamCorrente = this.getTeam();

        if (teamCorrente == null) {
            throw new IllegalStateException("Non appartieni a nessun team");
        }

        // Se è Viceleader, revoca il ruolo
        if (this.isViceLeader) {
            this.isViceLeader = false;
        }

        // Rimuove il membro dal team
        teamCorrente.removeMembro(this);

        // Rimuove il riferimento al team
        this.setTeam(null);
    }
}
