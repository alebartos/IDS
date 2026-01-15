package com.hackhub.enums;

/**
 * Enum che rappresenta i possibili stati di un hackathon.
 * <p>
 * Il ciclo di vita di un hackathon segue questo flusso:
 * IN_ISCRIZIONE -> IN_CORSO -> IN_VALUTAZIONE -> CONCLUSO
 */
public enum StatoHackathon {

    /** Iscrizioni aperte, i team possono registrarsi */
    IN_ISCRIZIONE,

    /** Hackathon in svolgimento, i team stanno lavorando ai progetti */
    IN_CORSO,

    /** Sottomissioni in fase di valutazione da parte dei giudici */
    IN_VALUTAZIONE,

    /** Hackathon terminato, vincitore proclamato */
    CONCLUSO
}
