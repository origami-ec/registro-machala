package org.origami.zull.repository;

import org.origami.zull.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsuarioNombre(String usuario);



}
