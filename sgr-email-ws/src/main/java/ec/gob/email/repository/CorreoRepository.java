package ec.gob.email.repository;

import ec.gob.email.entity.Correo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CorreoRepository extends JpaRepository<Correo, Long>,
        PagingAndSortingRepository<Correo, Long> {

}
