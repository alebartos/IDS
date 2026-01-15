package com.hackhub.model;

import com.hackhub.enums.StatoInvito;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un utente registrato nel sistema HackHub.
 * <p>
 * Un Utente puo':
 * - Creare un nuovo team (diventando Leader)
 * - Ricevere inviti da altri team
 * - Accettare o rifiutare inviti
 * <p>
 * Quando un Utente accetta un invito, diventa MembroTeam.
 * Quando un Utente crea un team, diventa Leader.
 * <p>
 * Estende: UtenteAstratto
 * Esteso da: MembroTeam
 */
public class Utente extends UtenteAstratto {

    /** Lista degli inviti ricevuti dall'utente */
    private List<Invito> invitiRicevuti;

    /** Riferimento al team a cui appartiene (null se non appartiene a nessun team) */
    private Team team;

    /**
     * Costruttore della classe Utente.
     *
     * @param nome     Il nome dell'utente
     * @param cognome  Il cognome dell'utente
     * @param email    L'email dell'utente (deve essere univoca)
     * @param password La password per l'autenticazione
     */
    public Utente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
        this.invitiRicevuti = new ArrayList<>();
        this.team = null;
    }

    // ==================== GETTER ====================

    /**
     * Restituisce la lista degli inviti ricevuti dall'utente.
     *
     * @return Lista degli inviti ricevuti
     */
    public List<Invito> getInvitiRicevuti() {
        return invitiRicevuti;
    }

    /**
     * Restituisce il team a cui appartiene l'utente.
     *
     * @return Il team dell'utente, o null se non appartiene a nessun team
     */
    public Team getTeam() {
        return team;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il team dell'utente.
     *
     * @param team Il team a cui associare l'utente
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Crea un nuovo team con l'utente come Leader.
     * <p>
     * Precondizioni:
     * - L'utente non deve appartenere già a un team
     * <p>
     * Postcondizioni:
     * - Viene creato un nuovo Team
     * - L'utente diventa il Leader del team
     *
     * @param nome        Il nome del team (deve essere univoco)
     * @param descrizione La descrizione del team (opzionale)
     * @return Il team appena creato
     * @throws IllegalStateException se l'utente appartiene già a un team
     */
    public Team creaTeam(String nome, String descrizione) {
        if (this.team != null) {
            throw new IllegalStateException("Appartieni già a un team");
        }

        // Crea il nuovo team
        Team nuovoTeam = new Team(nome, descrizione);

        // Crea il leader basandosi sui dati di questo utente
        Leader leader = new Leader(this.getNome(), this.getCognome(), this.getEmail(), this.getPassword());
        leader.setTeam(nuovoTeam);

        // Aggiunge il leader al team
        nuovoTeam.setLeader(leader);
        nuovoTeam.addMembro(leader);

        return nuovoTeam;
    }

    /**
     * Accetta un invito a unirsi a un team.
     * <p>
     * Precondizioni:
     * - L'invito deve essere in stato IN_ATTESA
     * - L'utente non deve appartenere già a un team
     * <p>
     * Postcondizioni:
     * - L'invito passa allo stato ACCETTATO
     * - L'utente diventa MembroTeam del team
     * - Tutti gli altri inviti pendenti vengono rifiutati automaticamente
     *
     * @param invito L'invito da accettare
     * @throws IllegalStateException se l'utente appartiene già a un team
     * @throws IllegalArgumentException se l'invito non è in stato IN_ATTESA
     */
    public void accettaInvito(Invito invito) {
        if (this.team != null) {
            throw new IllegalStateException("Appartieni già a un team");
        }

        if (invito.getStato() != StatoInvito.IN_ATTESA) {
            throw new IllegalArgumentException("L'invito non è in attesa");
        }

        // Accetta l'invito
        invito.accetta();

        // Associa l'utente al team
        this.team = invito.getTeam();

        // Rifiuta automaticamente tutti gli altri inviti pendenti
        for (Invito altroInvito : invitiRicevuti) {
            if (altroInvito != invito && altroInvito.getStato() == StatoInvito.IN_ATTESA) {
                altroInvito.rifiuta();
            }
        }
    }

    /**
     * Rifiuta un invito a unirsi a un team.
     * <p>
     * Precondizioni:
     * - L'invito deve essere in stato IN_ATTESA
     * <p>
     * Postcondizioni:
     * - L'invito passa allo stato RIFIUTATO
     *
     * @param invito L'invito da rifiutare
     * @throws IllegalArgumentException se l'invito non è in stato IN_ATTESA
     */
    public void rifiutaInvito(Invito invito) {
        if (invito.getStato() != StatoInvito.IN_ATTESA) {
            throw new IllegalArgumentException("L'invito non è in attesa");
        }

        invito.rifiuta();
    }

    /**
     * Aggiunge un invito alla lista degli inviti ricevuti.
     * Metodo usato internamente quando un Leader invia un invito.
     *
     * @param invito L'invito da aggiungere
     */
    public void aggiungiInvito(Invito invito) {
        this.invitiRicevuti.add(invito);
    }

    /**
     * Verifica se l'utente appartiene a un team.
     *
     * @return true se l'utente appartiene a un team, false altrimenti
     */
    public boolean haTeam() {
        return this.team != null;
    }

    /**
     * Restituisce gli inviti pendenti (in attesa di risposta).
     *
     * @return Lista degli inviti con stato IN_ATTESA
     */
    public List<Invito> getInvitiPendenti() {
        List<Invito> pendenti = new ArrayList<>();
        for (Invito invito : invitiRicevuti) {
            if (invito.getStato() == StatoInvito.IN_ATTESA) {
                pendenti.add(invito);
            }
        }
        return pendenti;
    }
}
