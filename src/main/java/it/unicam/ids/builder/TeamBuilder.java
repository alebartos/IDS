package it.unicam.ids.builder;

import it.unicam.ids.model.Team;

import java.time.LocalDate;

public class TeamBuilder {

    private String nome;
    private String descrizione;
    private LocalDate dataCreazione;
    private Leader leader;

    private TeamBuilder() {
        this.dataCreazione = LocalDate.now();
    }

    public static TeamBuilder newBuilder() {
        return new TeamBuilder();
    }

    public TeamBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public TeamBuilder descrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public TeamBuilder dataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    public TeamBuilder leader(Leader leader) {
        this.leader = leader;
        return this;
    }

    public Team build() {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalStateException("Il nome del team è obbligatorio");
        }
        if (leader == null) {
            throw new IllegalStateException("Il leader del team è obbligatorio");
        }

        Team team = new Team();
        team.setNome(nome);
        team.setDescrizione(descrizione);
        team.setDataCreazione(dataCreazione);
        team.setLeader(leader);
        return team;
    }
}
