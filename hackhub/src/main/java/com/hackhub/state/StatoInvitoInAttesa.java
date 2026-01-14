package com.hackhub.state;

import com.hackhub.model.Invito;
import java.time.LocalDate;

/**
 * Stato concreto che rappresenta un invito in attesa di risposta.
 *
 * In questo stato l'invito puo':
 * - Essere accettato (transizione a StatoInvitoAccettato)
 * - Essere rifiutato (transizione a StatoInvitoRifiutato)
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoInvitoInAttesa implements IStatoInvito {

    /** Nome dello stato */
    private static final String NOME_STATO = "IN_ATTESA";

    /**
     * Accetta l'invito.
     * Transizione valida: IN_ATTESA -> ACCETTATO
     *
     * @param invito L'invito da accettare
     */
    @Override
    public void accetta(Invito invito) {
        //implementare
    }

    /**
     * Rifiuta l'invito.
     * Transizione valida: IN_ATTESA -> RIFIUTATO
     *
     * @param invito L'invito da rifiutare
     */
    @Override
    public void rifiuta(Invito invito) {
        //implementare
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "IN_ATTESA"
     */
    @Override
    public String getNomeStato() {
        return NOME_STATO;
    }

    @Override
    public String toString() {
        return NOME_STATO;
    }
}
