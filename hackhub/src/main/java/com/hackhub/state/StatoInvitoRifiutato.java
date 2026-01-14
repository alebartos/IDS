package com.hackhub.state;

import com.hackhub.model.Invito;

/**
 * Stato concreto che rappresenta un invito rifiutato.
 *
 * Questo e' uno stato finale: nessuna transizione e' permessa.
 * Qualsiasi tentativo di accettare o rifiutare l'invito
 * genera un'eccezione.
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoInvitoRifiutato implements IStatoInvito {

    /** Nome dello stato */
    private static final String NOME_STATO = "RIFIUTATO";

    /**
     * Operazione non permessa: l'invito e' gia' rifiutato.
     *
     * @param invito L'invito
     * @throws IllegalStateException sempre, operazione non valida
     */
    @Override
    public void accetta(Invito invito) {
        throw new IllegalStateException("Non puoi accettare un invito gia' rifiutato");
    }

    /**
     * Operazione non permessa: l'invito e' gia' rifiutato.
     *
     * @param invito L'invito
     * @throws IllegalStateException sempre, operazione non valida
     */
    @Override
    public void rifiuta(Invito invito) {
        throw new IllegalStateException("L'invito e' gia' stato rifiutato");
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "RIFIUTATO"
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
