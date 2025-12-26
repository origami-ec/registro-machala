package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.TareasActivas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TareasActivasRepository extends JpaRepository<TareasActivas, Long>
        , PagingAndSortingRepository<TareasActivas, Long>,
        JpaSpecificationExecutor<TareasActivas> {


    @Query(value = "select * from tarea_activa ta where ( CONVERT(date,ta.fecha_ingreso) between  CONVERT(date,?1) and CONVERT(date,?2) and (ta.candidate like ?3 or ta.assignee = ?4))", nativeQuery = true)
    List<TareasActivas> getTareasByUsuario(String desde, String hasta, String usuario, String candidato);

    Long countByCodigo(String codigo);

    @Query(value = "select count(*) from dbo.tarea_activa ta where (ta.candidate = :usuario or ta.assignee = :usuario)", nativeQuery = true)
    Long countByUser(String usuario);

}
