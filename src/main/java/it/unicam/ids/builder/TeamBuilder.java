package it.unicam.ids.builder;

import it.unicam.ids.model.Team;

public class TeamBuilder {

    private String nome;
    private Long leaderId;

    private TeamBuilder() {
    }

    public static TeamBuilder newBuilder() {
        return new TeamBuilder();
    }

    public TeamBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public TeamBuilder leaderId(Long leaderId) {
        this.leaderId = leaderId;
        return this;
    }

    public Team build() {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalStateException("Il nome del team è obbligatorio");
        }
        if (leaderId == null) {
            throw new IllegalStateException("Il leaderId del team è obbligatorio");
        }

        Team team = new Team();
        team.setNome(nome);
        team.setLeaderId(leaderId);
        return team;
    }
}
