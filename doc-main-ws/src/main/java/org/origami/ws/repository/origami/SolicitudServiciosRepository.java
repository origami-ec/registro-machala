package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.SolicitudServicios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface SolicitudServiciosRepository extends JpaRepository<SolicitudServicios, Long>,
        PagingAndSortingRepository<SolicitudServicios, Long> {


    @Query("SELECT s FROM SolicitudServicios s  WHERE s.fechaCreacion BETWEEN ?1 AND ?2 AND s.usuarioCreacion = ?3")
    List<SolicitudServicios> findAllByFechaDepsUser(Date desde, Date hasta, Long usuario);

    @Query("SELECT s FROM SolicitudServicios s JOIN s.tipoServicio ts JOIN ts.departamento d WHERE s.fechaCreacion BETWEEN ?1 AND ?2 AND d.id = ?3")
    List<SolicitudServicios> findAllByFechaDeps(Date desde, Date hasta, Long departamento);

    @Query("SELECT s FROM SolicitudServicios s JOIN s.tipoServicio ts JOIN ts.departamento d WHERE s.fechaCreacion BETWEEN ?1 AND ?2")
    List<SolicitudServicios> findAllByFecha(Date desde, Date hasta);

    //@Query("SELECT s FROM SolicitudServicios s JOIN s.tipoServicio ts JOIN ts.departamento d WHERE s.fechaCreacion BETWEEN ?1 AND ?2")
    @Query("SELECT s FROM SolicitudServicios s JOIN s.tipoServicio ts JOIN ts.departamento d WHERE s.fechaCreacion BETWEEN ?1 AND ?2 AND s.usuarioCreacion = ?3")
    Page<SolicitudServicios> findAllByFechaCreacionBetweenAndUsuarioCreacion(Date desde, Date hasta, Long usuarioCreacion,Pageable pageable);

    @Query("SELECT s FROM SolicitudServicios s JOIN s.tipoServicio ts JOIN ts.departamento d WHERE s.fechaCreacion BETWEEN ?1 AND ?2 AND d.id = ?3")
    Page<SolicitudServicios> findAllByFechaCreacionBetweenAndDepartamentoId(Date desde, Date hasta, Long departamentoId,Pageable pageable);

    List<SolicitudServicios> findAllByTipoServicio_Departamento_Id(Long departamento);

    List<SolicitudServicios> findAllByUsuarioCreacion(Long usuario);

}
