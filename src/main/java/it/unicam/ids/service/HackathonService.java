package it.unicam.ids.service;


import it.unicam.ids.builder.HackathonBuilder;
import it.unicam.ids.dto.HackathonRequest;
import it.unicam.ids.model.*;
import it.unicam.ids.repository.HackathonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class HackathonService {

    @Autowired
    private HackathonRepository hackathonRepository;

    private HackathonBuilder hackathonBuilder;

    public HackathonService() {
        this.hackathonBuilder = HackathonBuilder.newBuilder();
    }

    @Transactional
    public Hackathon creaHackathon(Organizzatore organizzatore, HackathonRequest request) {
        if (organizzatore == null) {
            throw new IllegalArgumentException("L'organizzatore è obbligatorio");
        }

        if (esisteHackathonConNome(request.getNome())) {
            throw new IllegalArgumentException("Esiste già un hackathon con questo nome");
        }

        if (!validaDate(request.getDataInizio(), request.getDataFine(), request.getScadenzaIscrizioni())) {
            throw new IllegalArgumentException("Le date non valide");
        }

        Hackathon hackathon = HackathonBuilder.newBuilder()
                .nome(request.getNome())
                .descrizione(request.getDescrizione())
                .dataInizio(request.getDataInizio())
                .dataFine(request.getDataFine())
                .scadenzaIscrizioni(request.getScadenzaIscrizioni())
                .luogo(request.getLuogo())
                .regolamento(request.getRegolamento())
                .premio(request.getPremio())
                .maxMembriTeam(request.getMaxMembriTeam() != null ? request.getMaxMembriTeam() : 5)
                .build();

        return hackathonRepository.save(hackathon);
    }

    public boolean esisteHackathonConNome(String nome) {
        return hackathonRepository.existsByNome(nome);
    }

    public boolean validaDate(LocalDate dataInizio, LocalDate dataFine, LocalDate scadenzaIscrizioni) {
        if (dataInizio == null || dataFine == null || scadenzaIscrizioni == null) {
            return false;
        }

        if (scadenzaIscrizioni.isAfter(dataInizio)) {
            return false;
        }

        if (dataInizio.isAfter(dataFine) || dataInizio.isEqual(dataFine)) {
            return false;
        }

        return true;
    }

    public Hackathon getDettagliHackathon(Long hackathonId) {
        return hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
    }

    public int getMaxMembriTeam(Long hackathonId) {
        Hackathon hackathon = getDettagliHackathon(hackathonId);
        return hackathon.getMaxMembriTeam();
    }

    public boolean checkValiditaHackathon(Long hackathonId) {
        try {
            Hackathon hackathon = getDettagliHackathon(hackathonId);
            LocalDate oggi = LocalDate.now();

            if (oggi.isBefore(hackathon.getScadenzaIscrizioni())) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void iscriviTeam(Hackathon hackathon, Team team) {
        if (hackathon == null || team == null) {
            throw new IllegalArgumentException("Hackathon e Team sono obbligatori");
        }

        if (!checkValiditaHackathon(hackathon.getId())) {
            throw new IllegalArgumentException("Le iscrizioni per questo hackathon sono chiuse");
        }
    }
}
