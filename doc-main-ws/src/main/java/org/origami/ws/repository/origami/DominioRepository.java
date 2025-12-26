package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Dominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DominioRepository  extends JpaRepository<Dominio, Long> {
    List<Dominio> findAllByTipo(Short tipo);
}
