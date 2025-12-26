package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoTramiteRepository extends JpaRepository<TipoTramite, Long> {

    TipoTramite findByEstadoTrueAndActivitykey(String activityKey);

    TipoTramite findAllByAbreviatura(String abreviatrua);

    @Query("select t from TipoTramite t where t.abreviatura in (:abr) and t.id not in (:id)")
    List<TipoTramite> buquedaNotAbreviatrua(String abr, Long id);

    @Query("SELECT new org.origami.ws.entities.origami.TipoTramite(t.id, t.descripcion) from TipoTramite t where t.id =:id")
    TipoTramite findTipoTramite(Long id);

    @Query("SELECT new org.origami.ws.entities.origami.TipoTramite(t.id, t.descripcion, t.activitykey," +
            " t.estado, t.archivoBpmn, t.abreviatura,  t.dias," +
            " t.horas, t.minutos, t.segundos, t.interno, t.urlImagen, dep.id, dep.nombre,  dep.estado," +
            "dep.codigo, t.definicionTramite, t.color) from TipoTramite t " +
            "LEFT JOIN  t.departamento dep WHERE t.estado = true ORDER BY t.descripcion")
    List<TipoTramite> tramitesBpmn();


}
