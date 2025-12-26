package gob.ec.dinardap.repository;


import gob.ec.dinardap.entities.AclUser;
import org.springframework.data.repository.CrudRepository;

public interface AclUserRepository extends CrudRepository<AclUser, Integer> {

    AclUser findByUsuarioAndClave(String usuario, String clave);

    AclUser findByUsuario(String username);



}
