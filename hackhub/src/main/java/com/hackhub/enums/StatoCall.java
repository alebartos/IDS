package com.hackhub.enums;

/**
 * Enum che rappresenta i possibili stati di una call con un mentore.
 * <p>
 * Il ciclo di vita di una call segue questo flusso tipico:
 * PROPOSTA -> CONFERMATA -> PRENOTATA -> COMPLETATA
 * <p>
 * Una call puo' essere ANNULLATA in qualsiasi momento prima del completamento.
 */
public enum StatoCall {

    /** Call proposta dal mentore, in attesa di conferma dal team */
    PROPOSTA,

    /** Call confermata dal team, in attesa di prenotazione slot */
    CONFERMATA,

    /** Slot prenotato nel calendario, call programmata */
    PRENOTATA,

    /** Call effettuata con successo */
    COMPLETATA,

    /** Call annullata (dal mentore o dal team) */
    ANNULLATA
}
