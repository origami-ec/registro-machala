package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long>,
        PagingAndSortingRepository<Notificacion, Long>,
        JpaSpecificationExecutor<Notificacion> {

    @Query("SELECT a FROM Notificacion a WHERE a.tramite.id = :idTramite AND a.tipoNotificacion.clase = :clase ORDER BY a.secuencia")
    List<Notificacion> getAllByIdTramite(Long idTramite, Integer clase);


}
