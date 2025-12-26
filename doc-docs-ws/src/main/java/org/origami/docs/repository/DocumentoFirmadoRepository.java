package org.origami.docs.repository;

import org.origami.docs.entity.DocumentoFirmado;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentoFirmadoRepository extends MongoRepository<DocumentoFirmado, String> {

    List<DocumentoFirmado> findAllByUsuario_Usuario(String usuario);

}
