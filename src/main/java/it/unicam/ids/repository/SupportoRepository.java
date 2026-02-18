package it.unicam.ids.repository;

import it.unicam.ids.model.RichiestaSupporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportoRepository extends JpaRepository<RichiestaSupporto, Long> {

    List<RichiestaSupporto> findByHackathonId(Long hackathonId);
}
