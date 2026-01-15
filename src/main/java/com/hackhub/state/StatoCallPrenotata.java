package com.hackhub.state;

import com.hackhub.model.Call;

import java.time.LocalDateTime;

/**
 * Stato concreto che rappresenta una call prenotata con data e ora.
 *
 * In questo stato la call puo':
 * - Essere completata dopo l'esecuzione (transizione a COMPLETATA)
 * - Essere annullata (transizione a ANNULLATA)
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoCallPrenotata implements IStatoCall {

    /** Nome dello stato */
    private static final String NOME_STATO = "PRENOTATA";

    /**
     * Operazione non permessa: la call e' gia' confermata e prenotata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void conferma(Call call) {
        throw new IllegalStateException("La call e' gia' stata confermata");
    }

    /**
     * Operazione non permessa: la call e' gia' prenotata.
     *
     * @param call La call
     * @param dataOra La data e ora
     * @throws IllegalStateException sempre
     */
    @Override
    public void prenota(Call call, LocalDateTime dataOra) {
        throw new IllegalStateException("La call e' gia' stata prenotata");
    }

    /**
     * Completa la call.
     * Transizione valida: PRENOTATA -> COMPLETATA
     *
     * @param call La call da completare
     */
    @Override
    public void completa(Call call) {
        call.setStatoCall(new StatoCallCompletata());
    }

    /**
     * Annulla la call.
     * Transizione valida: PRENOTATA -> ANNULLATA
     *
     * @param call La call da annullare
     */
    @Override
    public void annulla(Call call) {
        call.setStatoCall(new StatoCallAnnullata());
    }

    /**
     * Restituisce il nome dello stato.
     *
     * @return "PRENOTATA"
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
