package it.unicam.ids.repository;

import it.unicam.ids.model.Segnalazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegnalazioneRepository extends JpaRepository<Segnalazione, Long> {

    List<Segnalazione> findByHackathonId(Long hackathonId);
}
