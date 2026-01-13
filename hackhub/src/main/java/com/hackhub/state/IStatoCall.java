package com.hackhub.state;

import com.hackhub.model.Call;
import java.time.LocalDateTime;

/**
 * Interfaccia per il pattern State applicato alle Call di mentoring.
 * <p>
 * Definisce le operazioni che possono essere eseguite su una call
 * in base al suo stato corrente. Ogni stato concreto implementa
 * questa interfaccia definendo il comportamento specifico.
 * <p>
 * Ciclo di vita della Call:
 * PROPOSTA -> CONFERMATA -> PRENOTATA -> COMPLETATA
 * (Puo' essere ANNULLATA da qualsiasi stato eccetto COMPLETATA)
 * <p>
 * Design Pattern: State
 */
public interface IStatoCall {

    /**
     * Conferma la call (il team accetta la proposta del mentore).
     *
     * @param call La call su cui operare
     * @throws IllegalStateException se l'operazione non è permessa
     */
    void conferma(Call call);

    /**
     * Prenota la call (imposta data e ora).
     *
     * @param call La call su cui operare
     * @param dataOra La data e ora della prenotazione
     * @throws IllegalStateException se l'operazione non è permessa
     */
    void prenota(Call call, LocalDateTime dataOra);

    /**
     * Completa la call (la sessione è stata effettuata).
     *
     * @param call La call su cui operare
     * @throws IllegalStateException se l'operazione non è permessa
     */
    void completa(Call call);

    /**
     * Annulla la call.
     *
     * @param call La call su cui operare
     * @throws IllegalStateException se l'operazione non è permessa
     */
    void annulla(Call call);

    /**
     * Restituisce il nome dello stato corrente.
     *
     * @return Il nome dello stato
     */
    String getNomeStato();
}
