package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.UsuarioResponsable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UsuarioResponsableRepository extends JpaRepository<UsuarioResponsable, Long>, PagingAndSortingRepository<UsuarioResponsable, Long> {

    UsuarioResponsable findByUsuarioAndDepartamento_Id(Long usuario, Long departamento);

    List<UsuarioResponsable> findAllByDepartamento_IdBcbgAndResponsable(Long departamento, Boolean responsable);

    List<UsuarioResponsable> findAllByDepartamento_IdBcbgAndJefe(Long departamento, Boolean jefe);

}

