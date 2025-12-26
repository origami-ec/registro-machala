package org.origami.ws.repository.security;


import org.origami.ws.entities.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT new org.origami.ws.entities.security.Usuario(u.id, u.nombreUsuario, u.clave, u.eliminado) "
            + "FROM Usuario u WHERE u.nombreUsuario = :usuario AND u.eliminado = false")
    Usuario findByUser(String usuario);

    @Query("SELECT new org.origami.ws.entities.security.Usuario(u.id, u.nombreUsuario, u.clave, u.eliminado) "
            + "FROM Usuario u WHERE u.usuarioNombre = :usuario AND u.eliminado = false")
    Usuario findByUsuarioNombre(String usuario);

    @Query("SELECT new org.origami.ws.entities.security.Usuario(u.id, u.nombreUsuario, u.clave, u.eliminado, u.usuarioRRHH) "
            + "FROM Usuario u WHERE u.usuarioNombre = :usuario AND u.clave = :clave AND u.eliminado = false")
    Usuario iniciarSesion(String usuario, String clave);

    @Query("SELECT u FROM Usuario u WHERE u.eliminado = false ORDER BY u.nombreUsuario")
    List<Usuario> findAllUsuarios();



    Usuario findTopByUsuarioRRHHOrderByIdDesc(Long recursoHumano);
}
