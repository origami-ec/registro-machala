package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.UsuarioResponsableServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioResponsableServicioRepository extends JpaRepository<UsuarioResponsableServicio, Long> {

    Optional<UsuarioResponsableServicio> findByUsuarioResponsable_IdAndServicio_Id(Long usuarioResponsable, Long servicioId);

    List<UsuarioResponsableServicio> findAllByUsuarioResponsable_Id(Long usuarioResponsable);

}
