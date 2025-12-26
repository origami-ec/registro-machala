package org.origami.docs.repository;

import org.origami.docs.entity.Tesauro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TesauroRepository extends MongoRepository<Tesauro, String> {
    @Query(value = "{'palabra': {$regex : ?0, $options: 'i'}}")
    Tesauro findByPalabra(String palabra);

    List<Tesauro> findAllByEstado(Boolean estado);
}
