package com.hackhub.state;

import com.hackhub.model.Call;
import java.time.LocalDateTime;

/**
 * Stato concreto che rappresenta una call completata.
 *
 * Questo è uno stato finale: nessuna transizione è permessa.
 * La call è stata eseguita con successo.
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoCallCompletata implements IStatoCall {

    /** Nome dello stato */
    private static final String NOME_STATO = "COMPLETATA";

    /**
     * Operazione non permessa: la call è già completata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void conferma(Call call) {
        throw new IllegalStateException("La call è già stata completata");
    }

    /**
     * Operazione non permessa: la call è già completata.
     *
     * @param call La call
     * @param dataOra La data e ora
     * @throws IllegalStateException sempre
     */
    @Override
    public void prenota(Call call, LocalDateTime dataOra) {
        throw new IllegalStateException("La call è già stata completata");
    }

    /**
     * Operazione non permessa: la call è già completata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void completa(Call call) {
        throw new IllegalStateException("La call è già stata completata");
    }

    /**
     * Operazione non permessa: non si può annullare una call completata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void annulla(Call call) {
        throw new IllegalStateException("Non puoi annullare una call già completata");
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "COMPLETATA"
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
