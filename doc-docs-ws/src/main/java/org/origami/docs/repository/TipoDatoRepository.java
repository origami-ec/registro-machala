package org.origami.docs.repository;

import org.origami.docs.entity.TipoDato;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TipoDatoRepository extends MongoRepository<TipoDato, String> {
}
