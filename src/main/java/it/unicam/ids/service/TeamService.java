package it.unicam.ids.service;


import it.unicam.ids.builder.TeamBuilder;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Leader;
import it.unicam.ids.model.Team;
import it.unicam.ids.repository.LeaderRepository;
import it.unicam.ids.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service per la gestione dei Team.
 * Utilizza constructor injection per facilitare il testing e la futura integrazione con Spring.
 */
public class TeamService {

    private final TeamRepository teamRepository;
    private final LeaderRepository leaderRepository;

    public TeamService(TeamRepository teamRepository, LeaderRepository leaderRepository) {
        this.teamRepository = teamRepository;
        this.leaderRepository = leaderRepository;
    }

    public Team creaTeam(TeamRequest request) {
        if (esisteTeamConNome(request.getNome())) {
            throw new IllegalArgumentException("Esiste già un team con questo nome");
        }

        Leader leader = leaderRepository.findById(request.getLeaderId())
                .orElseThrow(() -> new IllegalArgumentException("Leader non trovato"));

        if (teamRepository.findByLeader(leader).isPresent()) {
            throw new IllegalArgumentException("Il leader ha già un team");
        }

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

    public List<Object> getMembriTeam(Long teamId) {
        Team team = getDettagliTeam(teamId);
        return List.of();
    }

    public Team getTeamByLeader(Leader leader) {
        return teamRepository.findByLeader(leader)
                .orElse(null);
    }
}
