package ec.gob.email.repository;

import ec.gob.email.entity.CorreoArchivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorreoArchivoRepository extends JpaRepository<CorreoArchivo, Long> {

    List<CorreoArchivo> findByCorreo_Id(Long id);
}
