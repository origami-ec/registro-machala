package org.origami.ws.repository.rrhh;

import org.origami.ws.entities.rrhh.AccionPersonal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccionPersonalRepository extends JpaRepository<AccionPersonal, Long> {

     AccionPersonal findByDestinoIdOrderByIdDesc(Long usuarioDestino);

}
