package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.SolicitudDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SolicitudDepartamentoRepository extends JpaRepository<SolicitudDepartamento, Long>, PagingAndSortingRepository<SolicitudDepartamento, Long> {


}
