package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Observaciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacionRepository extends JpaRepository<Observaciones, Long> {
}
