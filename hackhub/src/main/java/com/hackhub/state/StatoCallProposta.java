package com.hackhub.state;

import com.hackhub.model.Call;
import java.time.LocalDateTime;

/**
 * Stato concreto che rappresenta una call proposta dal mentore.
 *
 * In questo stato la call puo':
 * - Essere confermata dal team (transizione a CONFERMATA)
 * - Essere annullata (transizione a ANNULLATA)
 *
 * Design Pattern: State (ConcreteState)
 */
public class StatoCallProposta implements IStatoCall {

    /** Nome dello stato */
    private static final String NOME_STATO = "PROPOSTA";

    /**
     * Conferma la call.
     * Transizione valida: PROPOSTA -> CONFERMATA
     *
     * @param call La call da confermare
     */
    @Override
    public void conferma(Call call) {
        call.setStatoCall(new StatoCallConfermata());
    }

    /**
     * Operazione non permessa: la call deve prima essere confermata.
     *
     * @param call La call
     * @param dataOra La data e ora
     * @throws IllegalStateException sempre
     */
    @Override
    public void prenota(Call call, LocalDateTime dataOra) {
        throw new IllegalStateException("La call deve essere confermata prima di essere prenotata");
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
     * Transizione valida: PROPOSTA -> ANNULLATA
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
     * @return "PROPOSTA"
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
