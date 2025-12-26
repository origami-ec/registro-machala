package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.SolicitudDocumentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolicitudDocumentosRespository extends JpaRepository<SolicitudDocumentos, Long> {

    @Query("SELECT new org.origami.ws.entities.origami.SolicitudDocumentos(solDoc.id, sol.id,solDoc.requisito.id ) " +
            "FROM SolicitudDocumentos solDoc join  solDoc.solicitud sol " +
            "WHERE solDoc.requisito.id =:idRequisito and sol.tramite.codigo =:codigo")
    SolicitudDocumentos findByRequisito_Solicitud_tramite_id(String codigo, Long idRequisito);
}
