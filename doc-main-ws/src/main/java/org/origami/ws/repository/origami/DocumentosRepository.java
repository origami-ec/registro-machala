package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentosRepository extends JpaRepository<Documentos, Long> {

    List<Documentos> findAllByUsuarioAndAndArchivoIsNotNullOrderByIdDesc(String usuario);

}
