package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.HistoricoTramites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HistoricoTramiteRepository extends JpaRepository<HistoricoTramites, Long>, PagingAndSortingRepository<HistoricoTramites, Long> {

    @Query("select t from HistoricoTramites t where t.id=:id")
    HistoricoTramites findIdTramiteHistorico(Long id);

    @Query("SELECT new org.origami.ws.entities.origami.HistoricoTramites(t.idProceso) from HistoricoTramites t where t.codigo=:codigo")
    HistoricoTramites findIdProceso(String codigo);

    HistoricoTramites findByCodigo(String codigo);


    @Query("SELECT new org.origami.ws.entities.origami.HistoricoTramites(ht.id,  ht.numTramite,ht.codigo) FROM HistoricoTramites ht where ht.idProceso=:idProceso")
    HistoricoTramites findByIdProceso(String idProceso);

    @Query("SELECT new org.origami.ws.entities.origami.HistoricoTramites(ht.id,  ht.numTramite,ht.codigo) FROM HistoricoTramites ht where ht.idProcesoTemp=:id")
    HistoricoTramites findByIdProcesoTemp(String id);

}
