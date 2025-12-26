package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Tramites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TramitesRepository extends JpaRepository<Tramites, Long>,
        PagingAndSortingRepository<Tramites, Long> {

    @Query(value = "SELECT new org.origami.ws.entities.origami.Tramites(t.idTipoTramite, count(t)) " +
            "FROM Tramites t WHERE t.startTime between :fechaDesde AND :fechaHasta " +
            "AND t.terminado=true GROUP BY t.idTipoTramite ORDER BY t.idTipoTramite.id")
    List<Tramites> findCustomerTramitesTerminados(@Param("fechaDesde") Date fechaDesde
            , @Param("fechaHasta") Date fechaHasta);

    @Query(value = "SELECT new org.origami.ws.entities.origami.Tramites(t.idTipoTramite, count(t)) " +
            "FROM Tramites t WHERE t.startTime between :fechaDesde AND :fechaHasta " +
            "AND t.terminado=false GROUP BY t.idTipoTramite ORDER BY t.idTipoTramite.id")
    List<Tramites> findCustomerTramitesPendientes(@Param("fechaDesde") Date fechaDesde
            , @Param("fechaHasta") Date fechaHasta);

    @Query(value = "SELECT new org.origami.ws.entities.origami.Tramites(t.participants,t.terminado,count(t)) FROM Tramites t " +
            "WHERE t.idTipoTramite.id=:idTipoTramite AND t.participants like %:participantes% AND t.terminado=:terminado " +
            "AND t.startTime between :fechaDesde AND :fechaHasta " +
            "GROUP BY t.terminado,t.participants")
    Optional<List<Tramites>> findAllCustomerByIdTipoTramite_IdAndParticipantsContainsAndStartTimeBetween(Long idTipoTramite, String participantes
            , Date fechaDesde, Date fechaHasta, Boolean terminado);

    @Query(value = "SELECT new org.origami.ws.entities.origami.Tramites(t.participants,t.terminado,count(t)) FROM Tramites t " +
            "WHERE t.solicitudServicios.id=:idServicio AND t.participants like %:participantes% AND t.terminado=:terminado " +
            "AND t.startTime between :fechaDesde AND :fechaHasta " +
            "GROUP BY t.terminado,t.participants")
    Optional<List<Tramites>> findAllCustomerByIdServicio_IdAndParticipantsContainsAndStartTimeBetween(Long idServicio, String participantes
            , Date fechaDesde, Date fechaHasta, Boolean terminado);
}
