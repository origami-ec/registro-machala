package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.ServiciosDepartamentoRequisitos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ServiciosDepartamentoItemsRequisitoRepository extends JpaRepository<ServiciosDepartamentoRequisitos, Long>, PagingAndSortingRepository<ServiciosDepartamentoRequisitos, Long> {

    @Query("SELECT new org.origami.ws.entities.origami.ServiciosDepartamentoRequisitos(req.id,req.nombre, req.descripcion, req.requerido, req.estado, req.formato_archivo) FROM ServiciosDepartamentoRequisitos req WHERE req.tipoTramite.id=:id AND req.nombre <> 'REQUISITOS DE INGRESO'")
    List<ServiciosDepartamentoRequisitos> findByTipoTramite_Id(Long id);

    List<ServiciosDepartamentoRequisitos> findAllByServiciosDepartamento_Abreviatura(String abreviatura);

}
