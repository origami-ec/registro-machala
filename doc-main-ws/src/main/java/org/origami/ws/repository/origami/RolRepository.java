package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RolRepository extends JpaRepository<Rol, Long>, PagingAndSortingRepository<Rol, Long> {
}
