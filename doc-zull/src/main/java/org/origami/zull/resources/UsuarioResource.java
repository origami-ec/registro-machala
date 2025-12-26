package org.origami.zull.resources;

import org.origami.zull.entity.Usuario;
import org.origami.zull.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("zull/cache/")
public class UsuarioResource {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PutMapping("users/update")
    public ResponseEntity<Usuario> updateUser(@RequestBody Usuario user) {
        usuarioRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("users/create")
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario user) {
        usuarioRepository.save(user);
        return ResponseEntity.ok(user);
    }
    
}
