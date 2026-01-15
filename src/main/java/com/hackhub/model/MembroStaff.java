package com.hackhub.model;

/**
 * Classe astratta che rappresenta un membro dello staff in HackHub.
 * <p>
 * I membri dello staff sono utenti con ruoli speciali nella gestione
 * degli hackathon. Non possono creare o unirsi a team.
 * <p>
 * Tipi di staff:
 * - Organizzatore: crea e gestisce hackathon
 * - Mentore: supporta i team con call di mentoring
 * - Giudice: valuta le sottomissioni dei team
 * <p>
 * Estende: UtenteAstratto
 * Esteso da: Organizzatore, Mentore, Giudice
 */
public abstract class MembroStaff extends UtenteAstratto {

    /** Ruolo del membro dello staff (es: "Organizzatore", "Mentore", "Giudice") */
    private String ruolo;

    /**
     * Costruttore della classe MembroStaff.
     *
     * @param nome     Il nome del membro dello staff
     * @param cognome  Il cognome del membro dello staff
     * @param email    L'email del membro dello staff
     * @param password La password del membro dello staff
     */
    public MembroStaff(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    // ==================== GETTER ====================

    /**
     * Restituisce il ruolo del membro dello staff.
     *
     * @return Il ruolo (es: "Organizzatore", "Mentore", "Giudice")
     */
    public String getRuolo() {
        return ruolo;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il ruolo del membro dello staff.
     *
     * @param ruolo Il ruolo da assegnare
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
