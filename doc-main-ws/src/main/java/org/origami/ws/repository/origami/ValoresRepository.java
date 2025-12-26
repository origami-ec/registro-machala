package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Valores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValoresRepository extends JpaRepository<Valores, Long> {
    Valores findByCode(String code);

    List<Valores> findAllByOrderByIdDesc();

    List<Valores> findAllByCodeContains(String code);
}
