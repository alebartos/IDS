package com.hackhub.model;

import com.hackhub.enums.StatoHackathon;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un Hackathon in HackHub.
 * <p>
 * Un Hackathon è un evento competitivo dove i team sviluppano progetti.
 * Caratteristiche:
 * - Ha un Organizzatore che lo gestisce
 * - Ha uno o più Mentori che supportano i team
 * - Ha un Giudice che valuta le sottomissioni
 * - I team si iscrivono e sottomettono progetti
 * <p>
 * Ciclo di vita:
 * IN_ISCRIZIONE -> IN_CORSO -> IN_VALUTAZIONE -> CONCLUSO
 */
public class Hackathon {

    /** Identificativo univoco dell'hackathon */
    private Long id;

    /** Contatore statico per generare ID univoci */
    private static Long contatoreId = 1L;

    /** Nome dell'hackathon */
    private String nome;

    /** Stato corrente dell'hackathon */
    private StatoHackathon stato;

    /** Data di inizio dell'hackathon */
    private LocalDate dataInizio;

    /** Data di fine dell'hackathon */
    private LocalDate dataFine;

    /** Scadenza per le iscrizioni */
    private LocalDate scadenzaIscrizioni;

    /** Luogo dell'hackathon */
    private String luogo;

    /** Regolamento dell'hackathon */
    private String regolamento;

    /** Premio per il vincitore */
    private double premio;

    /** Numero massimo di membri per team */
    private int maxMembriTeam;

    /** Organizzatore dell'hackathon */
    private Organizzatore organizzatore;

    /** Lista dei mentori associati */
    private List<Mentore> mentori;

    /** Giudice dell'hackathon */
    private Giudice giudice;

    /** Lista delle iscrizioni */
    private List<Iscrizione> iscrizioni;

    /** Lista delle sottomissioni */
    private List<Sottomissione> sottomissioni;

    /** Team vincitore (null se non ancora proclamato) */
    private Team vincitore;

    /**
     * Costruttore della classe Hackathon.
     *
     * @param nome               Nome dell'hackathon
     * @param dataInizio         Data di inizio
     * @param dataFine           Data di fine
     * @param scadenzaIscrizioni Scadenza per le iscrizioni
     */
    public Hackathon(String nome, LocalDate dataInizio, LocalDate dataFine, LocalDate scadenzaIscrizioni) {
        this.id = contatoreId++;
        this.nome = nome;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.stato = StatoHackathon.IN_ISCRIZIONE;
        this.mentori = new ArrayList<>();
        this.iscrizioni = new ArrayList<>();
        this.sottomissioni = new ArrayList<>();
        this.maxMembriTeam = 5; // Default
    }

    // ==================== GETTER ====================

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public StatoHackathon getStato() {
        return stato;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public LocalDate getScadenzaIscrizioni() {
        return scadenzaIscrizioni;
    }

    public String getLuogo() {
        return luogo;
    }

    public String getRegolamento() {
        return regolamento;
    }

    public double getPremio() {
        return premio;
    }

    public int getMaxMembriTeam() {
        return maxMembriTeam;
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }

    public List<Mentore> getMentori() {
        return mentori;
    }

    public Giudice getGiudice() {
        return giudice;
    }

    public List<Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public List<Sottomissione> getSottomissioni() {
        return sottomissioni;
    }

    public Team getVincitore() {
        return vincitore;
    }

    // ==================== SETTER ====================

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setStato(StatoHackathon stato) {
        this.stato = stato;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public void setScadenzaIscrizioni(LocalDate scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public void setPremio(double premio) {
        this.premio = premio;
    }

    public void setMaxMembriTeam(int maxMembriTeam) {
        this.maxMembriTeam = maxMembriTeam;
    }

    public void setOrganizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
    }

    public void setGiudice(Giudice giudice) {
        this.giudice = giudice;
        giudice.aggiungiHackathon(this);
    }

    public void setVincitore(Team vincitore) {
        this.vincitore = vincitore;
    }

    // ==================== OPERAZIONI ====================

    /**
     * Aggiunge un mentore all'hackathon.
     *
     * @param mentore Il mentore da aggiungere
     */
    public void addMentore(Mentore mentore) {
        this.mentori.add(mentore);
        mentore.aggiungiHackathon(this);
    }

    /**
     * Aggiunge un'iscrizione all'hackathon.
     *
     * @param iscrizione L'iscrizione da aggiungere
     */
    public void aggiungiIscrizione(Iscrizione iscrizione) {
        this.iscrizioni.add(iscrizione);
    }

    /**
     * Aggiunge una sottomissione all'hackathon.
     *
     * @param sottomissione La sottomissione da aggiungere
     */
    public void aggiungiSottomissione(Sottomissione sottomissione) {
        this.sottomissioni.add(sottomissione);
    }

    /**
     * Verifica se le iscrizioni sono ancora aperte.
     *
     * @return true se le iscrizioni sono aperte, false altrimenti
     */
    public boolean isIscrizioniAperte() {
        return stato == StatoHackathon.IN_ISCRIZIONE
                && LocalDate.now().isBefore(scadenzaIscrizioni.plusDays(1));
    }

    /**
     * Restituisce una rappresentazione testuale dell'hackathon.
     *
     * @return Stringa con info sull'hackathon
     */
    @Override
    public String toString() {
        return "Hackathon: " + nome + " [" + stato + "] (" + dataInizio + " - " + dataFine + ")";
    }
}
