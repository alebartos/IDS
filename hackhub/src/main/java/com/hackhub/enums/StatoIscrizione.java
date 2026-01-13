package com.hackhub.enums;

/**
 * Enum che rappresenta i possibili stati di un'iscrizione di un team a un hackathon.
 * <p>
 * Gli stati possibili sono:
 * - CONFERMATA: iscrizione valida e attiva
 * - ANNULLATA: iscrizione annullata volontariamente dal team
 * - SQUALIFICATA: team squalificato dall'organizzatore
 */
public enum StatoIscrizione {

    /** Iscrizione valida e attiva */
    CONFERMATA,

    /** Iscrizione annullata volontariamente dal team */
    ANNULLATA,

    /** Team squalificato dall'organizzatore per violazione regolamento */
    SQUALIFICATA
}
