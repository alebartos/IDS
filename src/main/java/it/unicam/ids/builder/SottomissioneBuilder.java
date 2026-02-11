package it.unicam.ids.builder;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;

/**
 * Builder per la creazione di oggetti Sottomissione.
 * Implementa il pattern Builder per una costruzione fluente degli oggetti Sottomissione.
 */
public class SottomissioneBuilder {

    private DatiProgetto datiProgetto;
    private StatoSottomissione stato;
    private Long teamId;
    private Long hackathonId;

    private SottomissioneBuilder() {
        this.stato = StatoSottomissione.BOZZA;
    }

    /**
     * Crea una nuova istanza del builder.
     * @return una nuova istanza di SottomissioneBuilder
     */
    public static SottomissioneBuilder newBuilder() {
        return new SottomissioneBuilder();
    }

    /**
     * Imposta i dati del progetto.
     * @param datiProgetto i dati del progetto
     * @return il builder per method chaining
     */
    public SottomissioneBuilder datiProgetto(DatiProgetto datiProgetto) {
        this.datiProgetto = datiProgetto;
        return this;
    }

    /**
     * Imposta lo stato della sottomissione.
     * @param stato lo stato della sottomissione
     * @return il builder per method chaining
     */
    public SottomissioneBuilder stato(StatoSottomissione stato) {
        this.stato = stato;
        return this;
    }

    /**
     * Imposta l'ID del team della sottomissione.
     * @param teamId l'ID del team che effettua la sottomissione
     * @return il builder per method chaining
     */
    public SottomissioneBuilder teamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    /**
     * Imposta l'ID dell'hackathon della sottomissione.
     * @param hackathonId l'ID dell'hackathon per cui si effettua la sottomissione
     * @return il builder per method chaining
     */
    public SottomissioneBuilder hackathonId(Long hackathonId) {
        this.hackathonId = hackathonId;
        return this;
    }

    /**
     * Costruisce l'oggetto Sottomissione con i valori impostati.
     * @return la nuova istanza di Sottomissione
     */
    public Sottomissione build() {
        Sottomissione sottomissione = new Sottomissione();
        sottomissione.setDatiProgetto(this.datiProgetto);
        sottomissione.setStato(this.stato);
        sottomissione.setTeamId(this.teamId);
        sottomissione.setHackathonId(this.hackathonId);
        return sottomissione;
    }
}
