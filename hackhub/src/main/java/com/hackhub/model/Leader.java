package com.hackhub.model;

import com.hackhub.enums.StatoInvito;
import java.time.LocalDate;

/**
 * Classe che rappresenta il Leader di un team in HackHub.
 *
 * Il Leader e' il membro con i privilegi massimi nel team. Puo':
 * - Invitare nuovi utenti al team
 * - Nominare un Viceleader
 * - Eliminare il team
 * - Iscrivere il team a un hackathon
 * - Abbandonare il team (con regole speciali)
 *
 * Estende: MembroTeam
 */
public class Leader extends MembroTeam {

    /**
     * Costruttore della classe Leader.
     *
     * @param nome     Il nome del leader
     * @param cognome  Il cognome del leader
     * @param email    L'email del leader
     * @param password La password del leader
     */
    public Leader(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    // ==================== OPERAZIONI ====================

    /**
     * Invia un invito a un utente per unirsi al team.
     *
     * Precondizioni:
     * - Il Leader deve avere un team
     * - L'utente destinatario non deve appartenere gia' a un team
     * - Non deve esistere gia' un invito pendente per questo utente da questo team
     *
     * Postcondizioni:
     * - Viene creato un nuovo Invito con stato IN_ATTESA
     * - L'invito viene aggiunto alla lista degli inviti del destinatario
     * - L'invito viene aggiunto alla lista degli inviti del team
     *
     * @param destinatario L'utente da invitare
     * @return L'invito creato
     * @throws IllegalStateException se il Leader non ha un team
     * @throws IllegalArgumentException se l'utente appartiene gia' a un team
     * @throws IllegalArgumentException se esiste gia' un invito pendente
     */
    public Invito invitaUtente(Utente destinatario) {
        Team teamCorrente = this.getTeam();

        if (teamCorrente == null) {
            throw new IllegalStateException("Non hai un team");
        }

        if (destinatario.haTeam()) {
            throw new IllegalArgumentException("L'utente appartiene gia' a un team");
        }

        // Verifica che non esista gia' un invito pendente
        for (Invito invito : teamCorrente.getInviti()) {
            if (invito.getDestinatario().equals(destinatario)
                    && invito.getStato() == StatoInvito.IN_ATTESA) {
                throw new IllegalArgumentException("Hai gia' inviato un invito a questo utente");
            }
        }

        // Crea il nuovo invito
        Invito nuovoInvito = new Invito(teamCorrente, destinatario);

        // Aggiunge l'invito al team e al destinatario
        teamCorrente.aggiungiInvito(nuovoInvito);
        destinatario.aggiungiInvito(nuovoInvito);

        return nuovoInvito;
    }

    /**
     * Nomina un membro del team come Viceleader.
     *
     * Precondizioni:
     * - Il membro deve appartenere allo stesso team del Leader
     * - Il membro non deve essere gia' Viceleader
     * - Il membro non deve essere il Leader stesso
     *
     * Postcondizioni:
     * - Se esiste gia' un Viceleader, perde il ruolo
     * - Il membro nominato diventa il nuovo Viceleader
     *
     * @param membro Il membro da nominare Viceleader
     * @throws IllegalArgumentException se il membro non appartiene al team
     * @throws IllegalArgumentException se il membro e' il Leader stesso
     */
    public void nominaViceleader(MembroTeam membro) {
        Team teamCorrente = this.getTeam();

        if (!teamCorrente.getMembri().contains(membro)) {
            throw new IllegalArgumentException("Il membro non appartiene al team");
        }

        if (membro == this) {
            throw new IllegalArgumentException("Il Leader non puo' essere anche Viceleader");
        }

        // Revoca il Viceleader attuale se esiste
        MembroTeam viceleaderAttuale = teamCorrente.getViceleader();
        if (viceleaderAttuale != null) {
            viceleaderAttuale.setViceLeader(false);
        }

        // Nomina il nuovo Viceleader
        membro.setViceLeader(true);
    }

    /**
     * Elimina il team.
     *
     * Precondizioni:
     * - Il Leader deve essere l'unico membro del team
     *
     * Postcondizioni:
     * - Tutti gli inviti pendenti vengono annullati
     * - Il team viene eliminato
     * - Il Leader non appartiene piu' a nessun team
     *
     * @throws IllegalStateException se ci sono altri membri nel team
     */
    public void eliminaTeam() {
        Team teamCorrente = this.getTeam();

        if (teamCorrente.countMembri() > 1) {
            throw new IllegalStateException("Non puoi eliminare il team se ci sono altri membri");
        }

        // Rifiuta tutti gli inviti pendenti
        for (Invito invito : teamCorrente.getInviti()) {
            if (invito.getStato() == StatoInvito.IN_ATTESA) {
                invito.rifiuta();
            }
        }

        // Rimuove il riferimento al team
        this.setTeam(null);
    }

    /**
     * Iscrive il team a un hackathon.
     *
     * Precondizioni:
     * - Il team non deve essere gia' iscritto all'hackathon
     * - L'hackathon deve essere in stato IN_ISCRIZIONE
     * - La scadenza delle iscrizioni non deve essere passata
     * - Il numero di membri del team deve rispettare il limite dell'hackathon
     *
     * Postcondizioni:
     * - Viene creata una nuova Iscrizione con stato CONFERMATA
     *
     * @param hackathon L'hackathon a cui iscriversi
     * @return L'iscrizione creata
     * @throws IllegalStateException se il team e' gia' iscritto
     * @throws IllegalStateException se le iscrizioni sono chiuse
     * @throws IllegalArgumentException se il team ha troppi membri
     */
    public Iscrizione iscriviTeam(Hackathon hackathon) {
        Team teamCorrente = this.getTeam();

        // Verifica che il team non sia gia' iscritto
        for (Iscrizione iscrizione : hackathon.getIscrizioni()) {
            if (iscrizione.getTeam().equals(teamCorrente)) {
                throw new IllegalStateException("Il team e' gia' iscritto a questo hackathon");
            }
        }

        // Verifica che le iscrizioni siano aperte
        if (!hackathon.isIscrizioniAperte()) {
            throw new IllegalStateException("Le iscrizioni sono chiuse");
        }

        // Verifica il limite di membri
        if (teamCorrente.countMembri() > hackathon.getMaxMembriTeam()) {
            throw new IllegalArgumentException(
                    "Il team ha troppi membri (max: " + hackathon.getMaxMembriTeam() + ")"
            );
        }

        // Crea l'iscrizione
        Iscrizione nuovaIscrizione = new Iscrizione(teamCorrente, hackathon);

        // Aggiunge l'iscrizione all'hackathon e al team
        hackathon.aggiungiIscrizione(nuovaIscrizione);
        teamCorrente.aggiungiIscrizione(nuovaIscrizione);

        return nuovaIscrizione;
    }

    /**
     * Abbandona il team con logica speciale per il Leader.
     *
     * La logica e':
     * - Se esiste un Viceleader: il Viceleader diventa Leader
     * - Se e' l'unico membro: il team viene eliminato
     * - Se ci sono altri membri ma nessun Viceleader: errore
     *
     * @throws IllegalStateException se ci sono membri ma nessun Viceleader
     */
    @Override
    public void abbandonaTeam() {
        Team teamCorrente = this.getTeam();

        if (teamCorrente == null) {
            throw new IllegalStateException("Non appartieni a nessun team");
        }

        MembroTeam viceleader = teamCorrente.getViceleader();

        if (viceleader != null) {
            // Il Viceleader diventa Leader
            // Nota: in una implementazione completa, dovremmo creare un nuovo Leader
            // Per ora, rimuoviamo semplicemente il Leader attuale
            teamCorrente.removeMembro(this);
            this.setTeam(null);

            // Qui andrebbe la logica per promuovere il Viceleader a Leader
            // Verra' implementata quando avremo il service layer

        } else if (teamCorrente.countMembri() == 1) {
            // Unico membro, elimina il team
            this.eliminaTeam();

        } else {
            // Ci sono altri membri ma nessun Viceleader
            throw new IllegalStateException("Devi nominare un Viceleader prima di abbandonare");
        }
    }
}
