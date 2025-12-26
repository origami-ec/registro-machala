package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Long>, PagingAndSortingRepository<RolUsuario, Long> {

    /**
     * Obtiene el rol por el nombre.
     *
     * @param nombre
     * @return
     */
    RolUsuario findFirstByRolNombreAndUsuarioOrderByIdDesc(String nombre, Long id);

    RolUsuario findByUsuarioAndRol_Id(Long idUsuario, Long idRol);

    List<RolUsuario> findAllByUsuario(Long idUsuario);
}
