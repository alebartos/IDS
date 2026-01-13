package com.hackhub.enums;

/**
 * Enum che rappresenta i possibili stati di un invito a un team.
 * <p>
 * Gli stati possibili sono:
 * - IN_ATTESA: l'invito e' stato inviato ma non ancora gestito dal destinatario
 * - ACCETTATO: il destinatario ha accettato l'invito e ora fa parte del team
 * - RIFIUTATO: il destinatario ha rifiutato l'invito
 */
public enum StatoInvito {

    /** Invito inviato, in attesa di risposta dal destinatario */
    IN_ATTESA,

    /** Invito accettato dall'utente, ora e' membro del team */
    ACCETTATO,

    /** Invito rifiutato dall'utente */
    RIFIUTATO
}
