package it.unicam.ids.repository;

import it.unicam.ids.model.Invito;
import it.unicam.ids.model.StatoInvito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitoRepository extends JpaRepository<Invito, Long> {

    Optional<Invito> findByTeamIdAndDestinatarioAndStato(Long teamId, Long destinatario, StatoInvito stato);

    List<Invito> findByDestinatario(Long destinatario);

    List<Invito> findByTeamId(Long teamId);

    List<Invito> findByDestinatarioAndStato(Long destinatario, StatoInvito stato);

    @Modifying
    @Query("UPDATE Invito i SET i.stato = 'RIFIUTATO' WHERE i.destinatario = :utenteId AND i.stato = 'IN_ATTESA'")
    void rifiutaAltriInviti(@Param("utenteId") Long utenteId);
}
