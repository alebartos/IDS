package it.unicam.ids.service;

import it.unicam.ids.builder.TeamBuilder;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Service per la gestione dei Team.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public TeamService(TeamRepository teamRepository, UtenteRepository utenteRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    public Team creaTeam(TeamRequest request) {
        if (esisteTeamConNome(request.getNome())) {
            throw new IllegalArgumentException("Esiste già un team con questo nome");
        }

        Utente leader = utenteRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (teamRepository.findByLeaderId(request.getLeaderId()).isPresent()) {
            throw new IllegalArgumentException("L'utente ha già un team come leader");
        }

        // Aggiungi il ruolo LEADER all'utente
        leader.addRuolo(Ruolo.LEADER);
        utenteRepository.save(leader);

        Team team = TeamBuilder.newBuilder()
                .nome(request.getNome())
                .descrizione(request.getDescrizione())
                .leader(leader)
                .dataCreazione(LocalDate.now())
                .build();

        return teamRepository.save(team);
    }

    public void iscriviTeam(Long teamId, Long hackathonId) {
    }

    public boolean esisteTeamConNome(String nome) {
        return teamRepository.existsByNome(nome);
    }

    public boolean validaTeam(Team team) {
        if (team == null) {
            return false;
        }
        if (team.getNome() == null || team.getNome().isEmpty()) {
            return false;
        }
        if (team.getLeader() == null) {
            return false;
        }
        return true;
    }

    public Team getDettagliTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public List<Utente> getMembriTeam(Long teamId) {
        Team team = getDettagliTeam(teamId);
        return new ArrayList<>(team.getMembri());
    }

    public Team getTeamByLeader(Utente leader) {
        return teamRepository.findByLeader(leader)
                .orElse(null);
    }

    /**
     * Aggiunge un membro al team. Solo il leader del team può aggiungere membri.
     * @param teamId ID del team
     * @param membroId ID dell'utente da aggiungere come membro
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere il leader)
     * @return il team aggiornato
     */
    public Team aggiungiMembro(Long teamId, Long membroId, Long richiedenteId) {
        if (membroId == null) {
            throw new IllegalArgumentException("L'ID del membro è obbligatorio");
        }

        Team team = getDettagliTeam(teamId);

        // Verifica che il richiedente sia il leader del team
        if (team.getLeader() == null || !team.getLeader().getId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può aggiungere membri");
        }

        Utente membro = utenteRepository.findById(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (team.hasMembro(membroId)) {
            throw new IllegalArgumentException("L'utente è già membro del team");
        }

        // Aggiungi il ruolo MEMBRO_TEAM all'utente
        membro.addRuolo(Ruolo.MEMBRO_TEAM);
        utenteRepository.save(membro);

        team.aggiungiMembro(membro);
        return teamRepository.save(team);
    }

    /**
     * Rimuove un membro dal team. Solo il leader del team può rimuovere membri.
     * @param teamId ID del team
     * @param membroId ID del membro da rimuovere
     * @param richiedenteId ID dell'utente che sta effettuando l'operazione (deve essere il leader)
     * @return il team aggiornato
     */
    public Team rimuoviMembro(Long teamId, Long membroId, Long richiedenteId) {
        if (membroId == null) {
            throw new IllegalArgumentException("L'ID del membro è obbligatorio");
        }

        Team team = getDettagliTeam(teamId);

        // Verifica che il richiedente sia il leader del team
        if (team.getLeader() == null || !team.getLeader().getId().equals(richiedenteId)) {
            throw new IllegalArgumentException("Solo il leader del team può rimuovere membri");
        }

        if (team.getLeader().getId().equals(membroId)) {
            throw new IllegalArgumentException("Non è possibile rimuovere il leader dal team");
        }

        if (!team.hasMembro(membroId)) {
            throw new IllegalArgumentException("L'utente non è membro del team");
        }

        team.rimuoviMembroById(membroId);
        return teamRepository.save(team);
    }
}
