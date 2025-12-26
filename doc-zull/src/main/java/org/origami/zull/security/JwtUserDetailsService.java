package org.origami.zull.security;


import org.origami.zull.entity.Usuario;
import org.origami.zull.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = service.find(new Usuario(username, null, Boolean.FALSE));
        if (usuario == null) {
            throw new UsernameNotFoundException(String.format("Usuario no encontrado", username));
        } else {
            return new User(username, usuario.getClave(),
                    new ArrayList<>());
        }

    }
}