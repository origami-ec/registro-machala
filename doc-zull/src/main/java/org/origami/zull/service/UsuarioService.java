package org.origami.zull.service;

import org.origami.zull.entity.Usuario;
import org.origami.zull.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario find(Usuario data) {
        return repository.findOne(Example.of(data)).orElse(null);
    }

    public List<Usuario> findAll(Usuario data) {
        return repository.findAll(Example.of(data));
    }

    public Usuario consultarUsuario(String username) {
        Usuario u = repository.findByUsuarioNombre(username);
        Usuario response = new Usuario();
        response.setId(u.getId());
        response.setUsuarioNombre(u.getUsuarioNombre());
        response.setNombreUsuario(u.getNombreUsuario());
        return response;
    }
}
