package it.unicam.ids.repository;

import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Utente> findByRuoliContaining(Ruolo ruolo);
}
