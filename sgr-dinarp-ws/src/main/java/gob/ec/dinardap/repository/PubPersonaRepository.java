package gob.ec.dinardap.repository;

import gob.ec.dinardap.entities.PubPersona;
import org.springframework.data.repository.CrudRepository;

public interface PubPersonaRepository extends CrudRepository<PubPersona, Long> {

    PubPersona findByCedRuc(String cedRuc);

}
