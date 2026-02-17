package it.unicam.ids.service;

import it.unicam.ids.dto.DatiProgetto;
import it.unicam.ids.model.Sottomissione;
import it.unicam.ids.model.StatoSottomissione;
import it.unicam.ids.builder.SottomissioneBuilder;
import it.unicam.ids.repository.SottomissioneRepository;

public class SottomissioneService {

    private final SottomissioneRepository sottomissioneRepo;

    public SottomissioneService(SottomissioneRepository sottomissioneRepo) {
        this.sottomissioneRepo = sottomissioneRepo;
    }

    public Sottomissione gestisciBozze(Long teamId, Long hackathonId) {
        Sottomissione sottomissione = sottomissioneRepo
                .findByTeamAndHackathon(teamId, hackathonId)
                .orElse(null);

        if (sottomissione != null) {
            if (sottomissione.getStato() == StatoSottomissione.CONSEGNATA) {
                throw new IllegalArgumentException("Sottomissione definitiva già inviata, non è possibile modificarla");
            }
            return sottomissione;
        }

        sottomissione = SottomissioneBuilder.newBuilder()
                .teamId(teamId)
                .hackathonId(hackathonId)
                .stato(StatoSottomissione.BOZZA)
                .build();

        return sottomissioneRepo.add(sottomissione);
    }

    public void checkValiditàLink(String linkRepository) {
        if (linkRepository == null || linkRepository.isEmpty()) {
            throw new IllegalArgumentException("Il link alla repository è obbligatorio");
        }
        if (!linkRepository.startsWith("http://") && !linkRepository.startsWith("https://")) {
            throw new IllegalArgumentException("Il link alla repository non è valido");
        }
    }

    public DatiProgetto creaDTO(String titolo, String descrizione, String linkRepository) {
        return new DatiProgetto(titolo, descrizione, linkRepository);
    }
}
