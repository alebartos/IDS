package it.unicam.ids.repository;

import it.unicam.ids.model.Organizzatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizzatoreRepository extends JpaRepository<Organizzatore, Long> {
}
