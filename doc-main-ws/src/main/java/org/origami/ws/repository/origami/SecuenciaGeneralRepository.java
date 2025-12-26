package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.SecuenciaGeneral;
import org.springframework.data.repository.CrudRepository;

public interface SecuenciaGeneralRepository extends CrudRepository<SecuenciaGeneral, Long> {

    SecuenciaGeneral findByCode(String code);

    SecuenciaGeneral findByCodeAndAnio(String code, Integer anio);

    SecuenciaGeneral findByCodeAndAnioAndDepartamento(String code, Integer anio, Long departamento);

}
