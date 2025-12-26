package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoNotificacionRepository extends JpaRepository<TipoNotificacion, Long> {

    List<TipoNotificacion> getAllByEstadoIsTrueAndClaseOrderByNombre(Integer clase);

}
