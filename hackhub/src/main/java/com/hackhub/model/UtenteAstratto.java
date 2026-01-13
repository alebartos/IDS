package com.hackhub.model;

/**
 * Classe astratta base per tutti gli utenti del sistema HackHub.
 * <p>
 * Questa classe definisce gli attributi e i comportamenti comuni a tutti
 * i tipi di utente: Utente normale, MembroTeam, Leader e membri dello Staff
 * (Organizzatore, Mentore, Giudice).
 * <p>
 * Gerarchia:
 *                     UtenteAstratto (questa classe)
 *                    /                \
 *               Utente              MembroStaff
 *                 |                 /    |    \
 *            MembroTeam    Organizzatore Mentore Giudice
 *                 |
 *              Leader
 */
public abstract class UtenteAstratto {

    /** Identificativo univoco dell'utente */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Nome dell'utente */
    private String nome;

    /** Cognome dell'utente */
    private String cognome;

    /** Email dell'utente (usata per il login, deve essere univoca) */
    private String email;

    /** Password dell'utente (per l'autenticazione) */
    private String password;

    /**
     * Costruttore della classe UtenteAstratto.
     *
     * @param nome     Il nome dell'utente
     * @param cognome  Il cognome dell'utente
     * @param email    L'email dell'utente (deve essere univoca nel sistema)
     * @param password La password per l'autenticazione
     */
    public UtenteAstratto(String nome, String cognome, String email, String password) {
        this.id = contatoreId++;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID univoco dell'utente.
     *
     * @return L'ID dell'utente
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return L'email dell'utente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return La password dell'utente
     */
    public String getPassword() {
        return password;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome Il nuovo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome Il nuovo cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta l'email dell'utente.
     *
     * @param email La nuova email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password La nuova password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Effettua il login dell'utente verificando email e password.
     *
     * @param email    L'email inserita per il login
     * @param password La password inserita per il login
     * @return true se le credenziali sono corrette, false altrimenti
     */
    public boolean effettuaLogin(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    /**
     * Effettua il logout dell'utente.
     * In questa versione base, il metodo non esegue operazioni specifiche.
     * Sara' esteso quando si implementera' la gestione delle sessioni.
     */
    public void effettuaLogout() {
        //IMPLEMENTAREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }

    /**
     * Restituisce una rappresentazione testuale dell'utente.
     *
     * @return Stringa con nome, cognome ed email dell'utente
     */
    @Override
    public String toString() {
        return nome + " " + cognome + " (" + email + ")";
    }
}
