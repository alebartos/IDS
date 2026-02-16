package it.unicam.ids.controller;

import it.unicam.ids.service.IscrizioneService;

import java.util.List;
import java.util.Map;

/**
 * Handler per le operazioni di iscrizione.
 */
public class IscrizioneHandler {

    private final IscrizioneService iscrizioneService;

    public IscrizioneHandler(IscrizioneService iscrizioneService) {
        this.iscrizioneService = iscrizioneService;
    }

    /**
     * Iscrive un team a un hackathon.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @return Result con l'esito dell'operazione
     */
    @SuppressWarnings("unchecked")
    public Result<String> iscriviTeam(Long teamId, Long hackathonId) {
        try {
            Map<String, Object> dati = iscrizioneService.selezionaPartecipanti(teamId, hackathonId);
            List<Long> membri = (List<Long>) dati.get("membri");
            iscrizioneService.iscriviTeam(teamId, hackathonId, membri);
            return Result.success("Team iscritto con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Recupera la lista dei membri del team e il limite massimo per la selezione dei partecipanti.
     * @param teamId ID del team
     * @param hackathonId ID dell'hackathon
     * @return Result con la mappa contenente "membri" e "maxMembriTeam"
     */
    public Result<Map<String, Object>> selezionaPartecipanti(Long teamId, Long hackathonId) {
        try {
            Map<String, Object> dati = iscrizioneService.selezionaPartecipanti(teamId, hackathonId);
            return Result.success(dati);
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * Valida la selezione dei partecipanti e iscrive il team all'hackathon.
     * @param teamId ID del team
     * @param selected lista degli ID dei membri selezionati
     * @param hackathonId ID dell'hackathon
     * @return Result con l'esito dell'operazione
     */
    public Result<String> selezionaPartecipanti(Long teamId, List<Long> selected, Long hackathonId) {
        try {
            iscrizioneService.iscriviTeam(teamId, hackathonId, selected);
            return Result.success("Partecipanti selezionati e team iscritto con successo");
        } catch (IllegalArgumentException e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
