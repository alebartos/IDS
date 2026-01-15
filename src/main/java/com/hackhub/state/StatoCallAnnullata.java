package com.hackhub.state;

import com.hackhub.model.Call;

import java.time.LocalDateTime;

/**
 * Stato concreto che rappresenta una call annullata.
 *
 * Questo è uno stato finale: nessuna transizione è permessa.
 * La call è stata cancellata e non può essere ripristinata.
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoCallAnnullata implements IStatoCall {

    /** Nome dello stato */
    private static final String NOME_STATO = "ANNULLATA";

    /**
     * Operazione non permessa: la call è stata annullata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void conferma(Call call) {
        throw new IllegalStateException("La call è stata annullata");
    }

    /**
     * Operazione non permessa: la call è stata annullata.
     *
     * @param call La call
     * @param dataOra La data e ora
     * @throws IllegalStateException sempre
     */
    @Override
    public void prenota(Call call, LocalDateTime dataOra) {
        throw new IllegalStateException("La call è stata annullata");
    }

    /**
     * Operazione non permessa: la call è stata annullata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void completa(Call call) {
        throw new IllegalStateException("La call è stata annullata");
    }

    /**
     * Operazione non permessa: la call è già annullata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void annulla(Call call) {
        throw new IllegalStateException("La call è già stata annullata");
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "ANNULLATA"
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
