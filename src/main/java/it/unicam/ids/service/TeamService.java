package it.unicam.ids.service;

import it.unicam.ids.builder.TeamBuilder;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;

/**
 * Service per la gestione dei Team.
 */
public class TeamService {

    private final TeamRepository teamRepo;
    private final InvitoRepository invitoRepo;
    private final UtenteRepository utenteRepo;

    public TeamService(TeamRepository teamRepo, InvitoRepository invitoRepo, UtenteRepository utenteRepo) {
        this.teamRepo = teamRepo;
        this.invitoRepo = invitoRepo;
        this.utenteRepo = utenteRepo;
    }

    /**
     * Crea un nuovo team.
     * @param nomeTeam il nome del team
     * @param leaderId l'ID del leader del team
     * @return il team creato
     */
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

        Team team = TeamBuilder.newBuilder()
                .nome(nomeTeam)
                .leaderId(leaderId)
                .build();

        return teamRepo.save(team);
    }

    /**
     * Verifica se esiste un team con il nome specificato.
     * @param nomeTeam il nome da verificare
     * @return true se esiste, false altrimenti
     */
    private boolean teamEsiste(String nomeTeam) {
        return teamRepo.existsByNome(nomeTeam);
    }

    /**
     * Rimuove un membro dal team. Solo il leader può rimuovere membri.
     * @param membroId l'ID del membro da rimuovere
     * @param leaderId l'ID del leader che effettua la rimozione
     * @return il team aggiornato
     */
    public Team rimuoviMembro(Long membroId, Long leaderId) {
        Team team = teamRepo.findByLeaderId(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));

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
        return teamRepo.save(team);
    }

    /**
     * Nomina un membro del team come viceleader.
     * @param leaderId l'ID del leader che effettua la nomina
     * @param membroId l'ID del membro da nominare viceleader
     * @return il team aggiornato
     */
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
        return teamRepo.save(team);
    }

    /**
     * Rimuove la nomina di viceleader di un membro del team.
     * @param leaderId l'ID del leader che effettua la revoca
     * @param membroId l'ID del membro a cui revocare il ruolo
     * @return il team aggiornato
     */
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
        return teamRepo.save(team);
    }

    /**
     * Verifica se un utente è membro di qualche team.
     * @param utenteId l'ID dell'utente da verificare
     * @return true se l'utente è membro di almeno un team, false altrimenti
     */
    public boolean findById(Long utenteId) {
        return teamRepo.findAll().stream()
                .anyMatch(t -> t.getMembri().contains(utenteId));
    }
}
