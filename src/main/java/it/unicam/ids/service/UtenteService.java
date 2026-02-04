package it.unicam.ids.service;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.UtenteRepository;

import java.util.List;

/**
 * Service per la gestione degli Utenti.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    /**
     * Registra un nuovo utente nel sistema.
     */
    public Utente registraUtente(String nome, String cognome, String email, String password) {
        if (utenteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Esiste già un utente con questa email");
        }

        Utente utente = new Utente(nome, cognome, email, password);
        return utenteRepository.save(utente);
    }

    /**
     * Cerca un utente per ID.
     */
    public Utente getUtente(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    /**
     * Cerca un utente per email.
     */
    public Utente getUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    /**
     * Aggiunge un ruolo a un utente.
     */
    public Utente addRuolo(Long utenteId, Ruolo ruolo) {
        Utente utente = getUtente(utenteId);

        if (utente.hasRuolo(ruolo)) {
            throw new IllegalArgumentException("L'utente ha già questo ruolo");
        }

        utente.addRuolo(ruolo);
        return utenteRepository.save(utente);
    }

    /**
     * Rimuove un ruolo da un utente.
     */
    public Utente deleteRuolo(Long utenteId, Ruolo ruolo) {
        Utente utente = getUtente(utenteId);

        if (!utente.hasRuolo(ruolo)) {
            throw new IllegalArgumentException("L'utente non ha questo ruolo");
        }

        if (ruolo == Ruolo.BASE) {
            throw new IllegalArgumentException("Non è possibile rimuovere il ruolo BASE");
        }

        utente.deleteRuolo(ruolo);
        return utenteRepository.save(utente);
    }

    /**
     * Verifica se un utente ha un determinato ruolo.
     */
    public boolean checkRuolo(Long utenteId, Ruolo ruolo) {
        Utente utente = getUtente(utenteId);
        return utente.hasRuolo(ruolo);
    }

    /**
     * Ottiene tutti gli utenti con un determinato ruolo.
     */
    public List<Utente> getUtentiByRuolo(Ruolo ruolo) {
        return utenteRepository.findByRuolo(ruolo);
    }

    /**
     * Ottiene tutti gli utenti.
     */
    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    /**
     * Verifica se esiste un utente con l'email specificata.
     */
    public boolean esisteUtenteConEmail(String email) {
        return utenteRepository.existsByEmail(email);
    }
}
