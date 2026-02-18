package it.unicam.ids.repository;

import it.unicam.ids.model.Sottomissione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SottomissioneRepository extends JpaRepository<Sottomissione, Long> {

    Optional<Sottomissione> findByTeamIdAndHackathonId(Long teamId, Long hackathonId);

    List<Sottomissione> findByTeamId(Long teamId);

    List<Sottomissione> findByHackathonId(Long hackathonId);
}
