package it.unicam.ids.builder;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;

import java.time.LocalDate;

public class InvitoBuilder {

    private Long teamId;
    private Long destinatario;
    private LocalDate dataInvio;
    private StatoInvito stato;

    private InvitoBuilder() {
        this.dataInvio = LocalDate.now();
        this.stato = StatoInvito.IN_ATTESA;
    }

    public static InvitoBuilder newBuilder() {
        return new InvitoBuilder();
    }

    public InvitoBuilder team(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    public InvitoBuilder destinatario(Long destinatario) {
        this.destinatario = destinatario;
        return this;
    }

    public InvitoBuilder dataInvio(LocalDate dataInvio) {
        this.dataInvio = dataInvio;
        return this;
    }

    public InvitoBuilder stato(StatoInvito stato) {
        this.stato = stato;
        return this;
    }

    public Invito build() {
        Invito invito = new Invito();
        invito.setTeamId(teamId);
        invito.setDestinatario(destinatario);
        invito.setDataInvio(dataInvio);
        invito.setStato(stato);
        return invito;
    }
}
