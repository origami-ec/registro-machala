package org.origami.docs.repository;

import org.origami.docs.entity.Formato;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FormatoRepository extends MongoRepository<Formato, String>, PagingAndSortingRepository<Formato,String> {

    Optional<Formato> findByFormato(String formato);

}
