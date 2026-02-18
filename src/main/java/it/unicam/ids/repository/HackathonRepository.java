package it.unicam.ids.repository;

import it.unicam.ids.model.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

    Optional<Hackathon> findByNome(String nome);

    Optional<Hackathon> findByOrganizzatoreId(Long organizzatoreId);
}
