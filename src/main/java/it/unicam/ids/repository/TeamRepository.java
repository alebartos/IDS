package it.unicam.ids.repository;

import it.unicam.ids.model.Team;
import it.unicam.ids.model.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByNome(String nome);

    Optional<Team> findByLeader(Leader leader);
}
