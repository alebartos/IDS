package com.hackhub.state;

import com.hackhub.model.Invito;

/**
 * Interfaccia per il pattern State applicato agli Inviti.
 * <p>
 * Definisce le operazioni che possono essere eseguite su un invito
 * in base al suo stato corrente. Ogni stato concreto implementa
 * questa interfaccia definendo il comportamento specifico.
 * <p>
 * Stati possibili:
 * - IN_ATTESA: stato iniziale, puo' essere accettato o rifiutato
 * - ACCETTATO: stato finale, nessuna transizione possibile
 * - RIFIUTATO: stato finale, nessuna transizione possibile
 * <p>
 * Design Pattern: State
 */
public interface IStatoInvito {

    /**
     * Gestisce l'accettazione dell'invito.
     *
     * @param invito L'invito su cui operare
     * @throws IllegalStateException se l'operazione non è permessa nello stato corrente
     */
    void accetta(Invito invito);

    /**
     * Gestisce il rifiuto dell'invito.
     *
     * @param invito L'invito su cui operare
     * @throws IllegalStateException se l'operazione non è permessa nello stato corrente
     */
    void rifiuta(Invito invito);

    /**
     * Restituisce il nome dello stato corrente.
     *
     * @return Il nome dello stato
     */
    String getNomeStato();
}
