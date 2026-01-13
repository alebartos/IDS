package com.hackhub.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un Team in HackHub.
 * <p>
 * Un Team e' un gruppo di utenti che partecipa agli hackathon.
 * Caratteristiche:
 * - Ha un Leader che lo gestisce
 * - Puo' avere un Viceleader
 * - Puo' avere uno o piu' membri
 * - Puo' iscriversi a hackathon
 * - Puo' sottomettere progetti
 * <p>
 * Relazioni:
 * - Composizione con MembroTeam (i membri non esistono senza team)
 * - Composizione con Invito (gli inviti non esistono senza team)
 * - Composizione con Sottomissione (le sottomissioni non esistono senza team)
 * - Composizione con Iscrizione (le iscrizioni non esistono senza team)
 */
public class Team {

    /** Identificativo univoco del team */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Nome del team (deve essere univoco) */
    private String nome;

    /** Descrizione del team */
    private String descrizione;

    /** Data di creazione del team */
    private LocalDate dataCreazione;

    /** Leader del team */
    private Leader leader;

    /** Lista dei membri del team (include il Leader) */
    private List<MembroTeam> membri;

    /** Lista degli inviti inviati dal team */
    private List<Invito> inviti;

    /** Lista delle iscrizioni del team agli hackathon */
    private List<Iscrizione> iscrizioni;

    /** Lista delle sottomissioni del team */
    private List<Sottomissione> sottomissioni;

    /**
     * Costruttore della classe Team.
     *
     * @param nome        Il nome del team (deve essere univoco)
     * @param descrizione La descrizione del team
     */
    public Team(String nome, String descrizione) {
        this.id = contatoreId++;
        this.nome = nome;
        this.descrizione = descrizione;
        this.dataCreazione = LocalDate.now();
        this.membri = new ArrayList<>();
        this.inviti = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
        this.sottomissioni = new ArrayList<>();
    }

    // ==================== GETTER ====================

    /**
     * Restituisce l'ID del team.
     *
     * @return L'ID del team
     */
    public Long getId() {
        return id;
    }

    /**
     * Restituisce il nome del team.
     *
     * @return Il nome del team
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce la descrizione del team.
     *
     * @return La descrizione del team
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce la data di creazione del team.
     *
     * @return La data di creazione
     */
    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Restituisce il Leader del team.
     *
     * @return Il Leader del team
     */
    public Leader getLeader() {
        return leader;
    }

    /**
     * Restituisce la lista dei membri del team.
     *
     * @return Lista dei membri
     */
    public List<MembroTeam> getMembri() {
        return membri;
    }

    /**
     * Restituisce la lista degli inviti inviati.
     *
     * @return Lista degli inviti
     */
    public List<Invito> getInviti() {
        return inviti;
    }

    /**
     * Restituisce la lista delle iscrizioni agli hackathon.
     *
     * @return Lista delle iscrizioni
     */
    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    /**
     * Restituisce la lista delle sottomissioni.
     *
     * @return Lista delle sottomissioni
     */
    public List<Sottomissione> getSottomissioni() {
        return sottomissioni;
    }

    // ==================== SETTER ====================

    /**
     * Imposta il nome del team.
     *
     * @param nome Il nuovo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta la descrizione del team.
     *
     * @param descrizione La nuova descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Imposta il Leader del team.
     *
     * @param leader Il Leader da impostare
     */
    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Aggiunge un membro al team.
     *
     * @param membro Il membro da aggiungere
     */
    public void addMembro(MembroTeam membro) {
        this.membri.add(membro);
        membro.setTeam(this);
    }

    /**
     * Rimuove un membro dal team.
     *
     * @param membro Il membro da rimuovere
     */
    public void removeMembro(MembroTeam membro) {
        this.membri.remove(membro);
    }

    /**
     * Restituisce il Viceleader del team, se presente.
     *
     * @return Il Viceleader, o null se non esiste
     */
    public MembroTeam getViceleader() {
        for (MembroTeam membro : membri) {
            if (membro.isViceLeader()) {
                return membro;
            }
        }
        return null;
    }

    /**
     * Verifica se il team ha un Viceleader.
     *
     * @return true se esiste un Viceleader, false altrimenti
     */
    public boolean hasViceleader() {
        return getViceleader() != null;
    }

    /**
     * Conta il numero di membri del team.
     *
     * @return Il numero di membri
     */
    public int countMembri() {
        return membri.size();
    }

    /**
     * Aggiunge un invito alla lista degli inviti del team.
     *
     * @param invito L'invito da aggiungere
     */
    public void aggiungiInvito(Invito invito) {
        this.inviti.add(invito);
    }

    /**
     * Aggiunge un'iscrizione alla lista delle iscrizioni del team.
     *
     * @param iscrizione L'iscrizione da aggiungere
     */
    public void aggiungiIscrizione(Iscrizione iscrizione) {
        this.iscrizioni.add(iscrizione);
    }

    /**
     * Aggiunge una sottomissione alla lista delle sottomissioni del team.
     *
     * @param sottomissione La sottomissione da aggiungere
     */
    public void aggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }

    /**
     * Restituisce una rappresentazione testuale del team.
     *
     * @return Stringa con nome e numero di membri
     */
    @Override
    public String toString() {
        return "Team: " + nome + " (" + countMembri() + " membri)";
    }
}
