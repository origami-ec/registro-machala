package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.ServiciosDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiciosDepartamentoItemRepository extends JpaRepository<ServiciosDepartamento, Long>, PagingAndSortingRepository<ServiciosDepartamento, Long> {
}
