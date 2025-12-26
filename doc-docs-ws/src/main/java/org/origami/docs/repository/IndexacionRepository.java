package org.origami.docs.repository;

import org.origami.docs.entity.Indexacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IndexacionRepository extends MongoRepository<Indexacion, String> {

    Indexacion findByDescripcion(String descripcion);

    List<Indexacion> findAllByEstado(Boolean estado);

}
