package com.hackhub.model;

import com.hackhub.enums.StatoInvito;
import java.time.LocalDate;

/**
 * Classe che rappresenta un Invito a unirsi a un team in HackHub.
 * <p>
 * Un Invito viene creato quando un Leader invita un Utente a unirsi al team.
 * L'utente puo' accettare o rifiutare l'invito.
 * <p>
 * Ciclo di vita:
 * 1. Creazione: stato IN_ATTESA
 * 2. Risposta: stato ACCETTATO o RIFIUTATO
 * <p>
 * Relazioni:
 * - Composizione con Team (l'invito non esiste senza team)
 * - Associazione con Utente (destinatario)
 */
public class Invito {

    /** Identificativo univoco dell'invito */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Stato corrente dell'invito */
    private StatoInvito stato;

    /** Data di invio dell'invito */
    private LocalDate dataInvio;

    /** Data di risposta all'invito (null se non ancora risposto) */
    private LocalDate dataRisposta;

    /** Team che ha inviato l'invito */
    private Team team;

    /** Utente destinatario dell'invito */
    private Utente destinatario;

    /**
     * Costruttore della classe Invito.
     *
     * @param team         Il team che invia l'invito
     * @param destinatario L'utente destinatario dell'invito
     */
    public Invito(Team team, Utente destinatario) {
        this.id = contatoreId++;
        this.team = team;
        this.destinatario = destinatario;
        this.stato = StatoInvito.IN_ATTESA;
        this.dataInvio = LocalDate.now();
        this.dataRisposta = null;
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID dell'invito.
     *
     * @return L'ID dell'invito
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce lo stato dell'invito.
     *
     * @return Lo stato corrente
     */
    public StatoInvito getStato() {
        return stato;
    }

    /**
     * Restituisce la data di invio.
     *
     * @return La data di invio
     */
    public LocalDate getDataInvio() {
        return dataInvio;
    }

    /**
     * Restituisce la data di risposta.
     *
     * @return La data di risposta, o null se non ancora risposto
     */
    public LocalDate getDataRisposta() {
        return dataRisposta;
    }

    /**
     * Restituisce il team che ha inviato l'invito.
     *
     * @return Il team mittente
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Restituisce l'utente destinatario dell'invito.
     *
     * @return L'utente destinatario
     */
    public Utente getDestinatario() {
        return destinatario;
    }

    // ==================== SETTER ====================

    /**
     * Imposta lo stato dell'invito.
     *
     * @param stato Il nuovo stato
     */
    public void setStato(StatoInvito stato) {
        this.stato = stato;
    }

    /**
     * Imposta la data di risposta.
     *
     * @param dataRisposta La data di risposta
     */
    public void setDataRisposta(LocalDate dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Accetta l'invito.
     * <p>
     * Precondizioni:
     * - Lo stato deve essere IN_ATTESA
     * <p>
     * Postcondizioni:
     * - Lo stato passa ad ACCETTATO
     * - La data di risposta viene impostata a oggi
     */
    public void accetta() {
        this.stato = StatoInvito.ACCETTATO;
        this.dataRisposta = LocalDate.now();
    }

    /**
     * Rifiuta l'invito.
     * <p>
     * Precondizioni:
     * - Lo stato deve essere IN_ATTESA
     * <p>
     * Postcondizioni:
     * - Lo stato passa a: RIFIUTATO
     * - La data di risposta viene impostata a oggi
     */
    public void rifiuta() {
        this.stato = StatoInvito.RIFIUTATO;
        this.dataRisposta = LocalDate.now();
    }

    /**
     * Restituisce una rappresentazione testuale dell'invito.
     *
     * @return Stringa con info sull'invito
     */
    @Override
    public String toString() {
        return "Invito da " + team.getNome() + " a " + destinatario.getEmail() + " [" + stato + "]";
    }
}
