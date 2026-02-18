package it.unicam.ids.repository;

import it.unicam.ids.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByNome(String nome);

    Optional<Team> findByLeaderId(Long leaderId);

    @Query("SELECT t FROM Team t WHERE t.leaderId = :utenteId OR :utenteId MEMBER OF t.membri")
    Optional<Team> findByUtenteId(@Param("utenteId") Long utenteId);
}
