package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.RegistroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclRegistroUserRepository extends JpaRepository<RegistroUsuario, Long> {

}
