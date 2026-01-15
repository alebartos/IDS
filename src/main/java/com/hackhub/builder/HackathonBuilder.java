package com.hackhub.builder;

import com.hackhub.enums.StatoHackathon;
import com.hackhub.model.Giudice;
import com.hackhub.model.Hackathon;
import com.hackhub.model.Mentore;
import com.hackhub.model.Organizzatore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder per la creazione di oggetti Hackathon.
 * <p>
 * Implementa il pattern Builder per semplificare la creazione di Hackathon
 * con molti parametri opzionali, rendendo il codice più leggibile e flessibile.
 * <p>
 * Esempio d'uso:
 * <p>
 * Hackathon hackathon = new HackathonBuilder()
 *     .nome("HackUnicam 2025")
 *     .dataInizio(LocalDate.of(2025, 6, 1))
 *     .dataFine(LocalDate.of(2025, 6, 3))
 *     .scadenzaIscrizioni(LocalDate.of(2025, 5, 15))
 *     .luogo("Camerino")
 *     .premio(5000.0)
 *     .maxMembriTeam(4)
 *     .organizzatore(org)
 *     .giudice(giudice)
 *     .addMentore(mentore1)
 *     .addMentore(mentore2)
 *     .build();
 * </pre>
 *
 * Design Pattern: Builder
 */
public class HackathonBuilder {

    /** Nome dell'hackathon */
    private String nome;

    /** Data di inizio */
    private LocalDate dataInizio;

    /** Data di fine */
    private LocalDate dataFine;

    /** Scadenza iscrizioni */
    private LocalDate scadenzaIscrizioni;

    /** Luogo dell'evento */
    private String luogo;

    /** Regolamento */
    private String regolamento;

    /** Premio in palio */
    private double premio;

    /** Numero massimo membri per team */
    private int maxMembriTeam = 5;

    /** Stato iniziale */
    private StatoHackathon stato = StatoHackathon.IN_ISCRIZIONE;

    /** Organizzatore dell'hackathon */
    private Organizzatore organizzatore;

    /** Giudice dell'hackathon */
    private Giudice giudice;

    /** Lista dei mentori */
    private List<Mentore> mentori = new ArrayList<>();

    /**
     * Costruttore di default.
     * Inizializza un nuovo builder con valori di default.
     */
    public HackathonBuilder() {
        // Costruttore vuoto - i valori di default sono già inizializzati
    }

    /**
     * Metodo factory per iniziare la costruzione.
     *
     * @return Una nuova istanza di HackathonBuilder
     */
    public static HackathonBuilder nuovo() {
        return new HackathonBuilder();
    }

    /**
     * Imposta il nome dell'hackathon.
     *
     * @param nome Il nome dell'hackathon
     * @return Questo builder per method chaining
     */
    public HackathonBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    /**
     * Imposta la data di inizio.
     *
     * @param dataInizio La data di inizio
     * @return Questo builder per method chaining
     */
    public HackathonBuilder dataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
        return this;
    }

    /**
     * Imposta la data di fine.
     *
     * @param dataFine La data di fine
     * @return Questo builder per method chaining
     */
    public HackathonBuilder dataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
        return this;
    }

    /**
     * Imposta la scadenza delle iscrizioni.
     *
     * @param scadenzaIscrizioni La data di scadenza
     * @return Questo builder per method chaining
     */
    public HackathonBuilder scadenzaIscrizioni(LocalDate scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        return this;
    }

    /**
     * Imposta il luogo dell'hackathon.
     *
     * @param luogo Il luogo
     * @return Questo builder per method chaining
     */
    public HackathonBuilder luogo(String luogo) {
        this.luogo = luogo;
        return this;
    }

    /**
     * Imposta il regolamento dell'hackathon.
     *
     * @param regolamento Il regolamento
     * @return Questo builder per method chaining
     */
    public HackathonBuilder regolamento(String regolamento) {
        this.regolamento = regolamento;
        return this;
    }

    /**
     * Imposta il premio in palio.
     *
     * @param premio L'importo del premio
     * @return Questo builder per method chaining
     */
    public HackathonBuilder premio(double premio) {
        this.premio = premio;
        return this;
    }

    /**
     * Imposta il numero massimo di membri per team.
     *
     * @param maxMembriTeam Il numero massimo
     * @return Questo builder per method chaining
     */
    public HackathonBuilder maxMembriTeam(int maxMembriTeam) {
        this.maxMembriTeam = maxMembriTeam;
        return this;
    }

    /**
     * Imposta lo stato iniziale dell'hackathon.
     *
     * @param stato Lo stato
     * @return Questo builder per method chaining
     */
    public HackathonBuilder stato(StatoHackathon stato) {
        this.stato = stato;
        return this;
    }

    /**
     * Imposta l'organizzatore dell'hackathon.
     *
     * @param organizzatore L'organizzatore
     * @return Questo builder per method chaining
     */
    public HackathonBuilder organizzatore(Organizzatore organizzatore) {
        this.organizzatore = organizzatore;
        return this;
    }

    /**
     * Imposta il giudice dell'hackathon.
     *
     * @param giudice Il giudice
     * @return Questo builder per method chaining
     */
    public HackathonBuilder giudice(Giudice giudice) {
        this.giudice = giudice;
        return this;
    }

    /**
     * Aggiunge un mentore all'hackathon.
     *
     * @param mentore Il mentore da aggiungere
     * @return Questo builder per method chaining
     */
    public HackathonBuilder addMentore(Mentore mentore) {
        this.mentori.add(mentore);
        return this;
    }

    /**
     * Costruisce l'oggetto Hackathon con i parametri configurati.
     *
     * @return L'hackathon costruito
     * @throws IllegalStateException se mancano parametri obbligatori
     */
    public Hackathon build() {
        // Validazione parametri obbligatori
        if (nome == null || nome.isEmpty()) {
            throw new IllegalStateException("Il nome dell'hackathon e' obbligatorio");
        }
        if (dataInizio == null) {
            throw new IllegalStateException("La data di inizio e' obbligatoria");
        }
        if (dataFine == null) {
            throw new IllegalStateException("La data di fine e' obbligatoria");
        }
        if (scadenzaIscrizioni == null) {
            throw new IllegalStateException("La scadenza iscrizioni e' obbligatoria");
        }

        // Validazione logica delle date
        if (dataFine.isBefore(dataInizio)) {
            throw new IllegalStateException("La data di fine non puo' essere prima della data di inizio");
        }
        if (scadenzaIscrizioni.isAfter(dataInizio)) {
            throw new IllegalStateException("La scadenza iscrizioni deve essere prima della data di inizio");
        }

        // Creazione dell'hackathon
        Hackathon hackathon = new Hackathon(nome, dataInizio, dataFine, scadenzaIscrizioni);

        // Impostazione parametri opzionali
        if (luogo != null) {
            hackathon.setLuogo(luogo);
        }
        if (regolamento != null) {
            hackathon.setRegolamento(regolamento);
        }
        if (premio > 0) {
            hackathon.setPremio(premio);
        }
        if (maxMembriTeam > 0) {
            hackathon.setMaxMembriTeam(maxMembriTeam);
        }
        if (stato != null) {
            hackathon.setStato(stato);
        }
        if (organizzatore != null) {
            hackathon.setOrganizzatore(organizzatore);
        }
        if (giudice != null) {
            hackathon.setGiudice(giudice);
        }

        // Aggiunta mentori
        for (Mentore mentore : mentori) {
            hackathon.addMentore(mentore);
        }

        return hackathon;
    }
}
