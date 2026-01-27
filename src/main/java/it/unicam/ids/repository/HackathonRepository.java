package it.unicam.ids.repository;


import it.unicam.ids.model.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

    boolean existsByNome(String nome);
}
