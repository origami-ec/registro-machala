package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.TareasHistoricas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TareasHistoricasRepository extends JpaRepository<TareasHistoricas, Long>,
        PagingAndSortingRepository<TareasHistoricas, Long>,
        JpaSpecificationExecutor<TareasHistoricas> {

    //@Query("SELECT th FROM TareasHistoricas th WHERE th.assignee = :usuario AND th.createTime BETWEEN :desde and :hasta")
    //List<TareasHistoricas> findAllByAssigneeLikeAndCreateTimeBetween(String usuario, Date desde, Date hasta);

    @Query(value = "select * from dbo.tarea_historica ta where ta.assignee = ?1 and " +
            "CONVERT(date,ta.fecha_ingreso) between CONVERT(date,?2) and CONVERT(date,?3)", nativeQuery = true)
    List<TareasHistoricas> getTareasRealizadasByUsuarioYFechas(String usuario, String desde, String hasta);

    @Query("SELECT new org.origami.ws.entities.origami.TareasHistoricas(th.idTipoTramite, th.assignee, th.terminado, " +
            "th.periodo, count(th)) FROM TareasHistoricas th GROUP BY th.idTipoTramite, th.assignee," +
            "th.terminado, th.periodo")
    List<TareasHistoricas> customerFindAllTareasGroupBy();

}
