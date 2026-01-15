package com.hackhub.state;

import com.hackhub.model.Invito;

/**
 * Stato concreto che rappresenta un invito accettato.
 *
 * Questo è uno stato finale: nessuna transizione è permessa.
 * Qualsiasi tentativo di accettare o rifiutare l'invito
 * genera un'eccezione.
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoInvitoAccettato implements IStatoInvito {

    /** Nome dello stato */
    private static final String NOME_STATO = "ACCETTATO";

    /**
     * Operazione non permessa: l'invito è già accettato.
     *
     * @param invito L'invito
     * @throws IllegalStateException sempre, operazione non valida
     */
    @Override
    public void accetta(Invito invito) {
        throw new IllegalStateException("L'invito e' gia' stato accettato");
    }

    /**
     * Operazione non permessa: l'invito è già accettato.
     *
     * @param invito L'invito
     * @throws IllegalStateException sempre, operazione non valida
     */
    @Override
    public void rifiuta(Invito invito) {
        throw new IllegalStateException("Non puoi rifiutare un invito già accettato");
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "ACCETTATO"
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
