package it.unicam.ids.service;

import it.unicam.ids.builder.TeamBuilder;
import it.unicam.ids.dto.TeamRequest;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;

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
                .leaderId(request.getLeaderId())
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
        if (team.getLeaderId() == null) {
            return false;
        }
        return true;
    }

    public Team getDettagliTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public Team getTeamByLeaderId(Long leaderId) {
        return teamRepository.findByLeaderId(leaderId)
                .orElse(null);
    }
}
