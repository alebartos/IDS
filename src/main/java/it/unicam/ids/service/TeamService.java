package it.unicam.ids.service;

import it.unicam.ids.builder.TeamBuilder;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepo;
    private final InvitoRepository invitoRepo;
    private final UtenteRepository utenteRepo;
    private final HackathonRepository hackathonRepo;

    public TeamService(TeamRepository teamRepo, InvitoRepository invitoRepo, UtenteRepository utenteRepo,
                       HackathonRepository hackathonRepo) {
        this.teamRepo = teamRepo;
        this.invitoRepo = invitoRepo;
        this.utenteRepo = utenteRepo;
        this.hackathonRepo = hackathonRepo;
    }

    public Team createTeam(String nomeTeam, Long leaderId) {
        if (teamEsiste(nomeTeam)) {
            throw new IllegalArgumentException("Esiste già un team con questo nome");
        }
        Utente leader = utenteRepo.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        if (teamRepo.findByLeaderId(leaderId).isPresent()) {
            throw new IllegalArgumentException("L'utente ha già un team come leader");
        }
        leader.getRuoli().add(Ruolo.LEADER);
        utenteRepo.save(leader);
        Team team = TeamBuilder.newBuilder().nome(nomeTeam).leaderId(leaderId).build();
        team.getMembri().add(leaderId);
        return teamRepo.save(team);
    }

    private boolean teamEsiste(String nomeTeam) {
        return teamRepo.findByNome(nomeTeam).isPresent();
    }

    public Team rimuoviMembro(Long membroId, Long leaderId) {
        Team team = teamRepo.findByLeaderId(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        for (Hackathon h : hackathonRepo.findAll()) {
            if (h.getTeamIds().contains(team.getId())) {
                throw new IllegalArgumentException("Non puoi rimuovere un membro: il team è iscritto a un hackathon");
            }
        }
        if (leaderId.equals(membroId)) {
            throw new IllegalArgumentException("Non puoi rimuovere te stesso");
        }
        Utente membro = utenteRepo.findById(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Membro non trovato"));
        if (!team.getMembri().contains(membroId)) {
            throw new IllegalArgumentException("L'utente non è membro del team");
        }
        if (membro.getRuoli().contains(Ruolo.VICELEADER) && team.getViceleaderId() != null && team.getViceleaderId().equals(membroId)) {
            throw new IllegalArgumentException("Devi indicare un nuovo viceleader prima di rimuovere quello attuale!");
        }
        membro.getRuoli().remove(Ruolo.MEMBRO_TEAM);
        utenteRepo.save(membro);
        team.getMembri().remove(membroId);
        teamRepo.save(team);
        return team;
    }

    public Team nominaViceleader(Long leaderId, Long membroId) {
        Team team = teamRepo.findByLeaderId(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        Utente membro = utenteRepo.findById(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Membro non trovato"));
        if (!team.getMembri().contains(membroId)) {
            throw new IllegalArgumentException("L'utente non è membro del team");
        }
        if (team.getViceleaderId() != null) {
            throw new IllegalArgumentException("Esiste già un viceleader. Revocalo prima");
        }
        membro.getRuoli().add(Ruolo.VICELEADER);
        utenteRepo.save(membro);
        team.setViceleaderId(membroId);
        teamRepo.save(team);
        return team;
    }

    public Team removeViceleader(Long leaderId, Long membroId) {
        Team team = teamRepo.findByLeaderId(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
        Utente membro = utenteRepo.findById(membroId)
                .orElseThrow(() -> new IllegalArgumentException("Membro non trovato"));
        if (!membro.getRuoli().contains(Ruolo.VICELEADER)) {
            throw new IllegalArgumentException("L'utente non è viceleader");
        }
        membro.getRuoli().remove(Ruolo.VICELEADER);
        utenteRepo.save(membro);
        team.setViceleaderId(null);
        teamRepo.save(team);
        return team;
    }

    public boolean findById(Long utenteId) {
        return teamRepo.findAll().stream().anyMatch(t -> t.getMembri().contains(utenteId));
    }

    public boolean checkRuolo(Ruolo ruolo) {
        return ruolo != null;
    }
}
