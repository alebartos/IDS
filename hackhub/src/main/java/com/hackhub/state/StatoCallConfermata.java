package com.hackhub.state;

import com.hackhub.model.Call;
import java.time.LocalDateTime;

/**
 * Stato concreto che rappresenta una call confermata dal team.
 *
 * In questo stato la call può:
 * - Essere prenotata con data e ora (transizione a PRENOTATA)
 * - Essere annullata (transizione a ANNULLATA)
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoCallConfermata implements IStatoCall {

    /** Nome dello stato */
    private static final String NOME_STATO = "CONFERMATA";

    /**
     * Operazione non permessa: la call è già confermata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void conferma(Call call) {
        throw new IllegalStateException("La call è già stata confermata");
    }

    /**
     * Prenota la call con data e ora.
     * Transizione valida: CONFERMATA -> PRENOTATA
     *
     * @param call La call da prenotare
     * @param dataOra La data e ora della prenotazione
     */
    @Override
    public void prenota(Call call, LocalDateTime dataOra) {
        // da implementare
    }

    /**
     * Operazione non permessa: la call deve prima essere prenotata.
     *
     * @param call La call
     * @throws IllegalStateException sempre
     */
    @Override
    public void completa(Call call) {
        throw new IllegalStateException("La call deve essere prenotata prima di essere completata");
    }

    /**
     * Annulla la call.
     * Transizione valida: CONFERMATA -> ANNULLATA
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
     * @return "CONFERMATA"
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
