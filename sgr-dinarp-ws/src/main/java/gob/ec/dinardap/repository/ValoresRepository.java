package gob.ec.dinardap.repository;

import gob.ec.dinardap.entities.Valores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValoresRepository extends JpaRepository<Valores, Long> {

    Valores findByCode(String codigo);

}
