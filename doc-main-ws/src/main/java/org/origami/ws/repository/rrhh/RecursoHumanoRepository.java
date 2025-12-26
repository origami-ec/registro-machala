package org.origami.ws.repository.rrhh;

import org.origami.ws.entities.rrhh.RecursoHumano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoHumanoRepository extends JpaRepository<RecursoHumano, Long> {

    List<RecursoHumano> findAllByTipoLugarAndLugarTrabajo(Long tipoLugar, Long lugarTrabajo);

    RecursoHumano findTopByPersonaRH_CedulaOrderByIdDesc(String cedula);

}
