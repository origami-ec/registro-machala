package org.origami.ws.service;

import org.origami.ws.async.RemoteService;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.rrhh.RecursoHumano;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.models.Data;
import org.origami.ws.repository.security.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private RecursoHumanoService recursoHumanoService;
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private UsuarioResponsableService usuarioResponsableService;

    private Map<String, List> map;

    public Usuario iniciarSesion(Usuario data) {
        Usuario u = repository.iniciarSesion(data.getUsuarioNombre(), data.getClave());
        if (u != null) {
            try {
                RecursoHumano rh = recursoHumanoService.find(new RecursoHumano(u.getUsuarioRRHH()));
                u.setRecursoHumano(rh);
                if (rh != null) {
                    Departamento dep = departamentoService.usuariosXdepartamentos(rh.getLugarTrabajo(), rh.getTipoLugar());
                    if (dep != null) {
                        u.setDepartamento(dep.getNombre());
                        u.setDepartamentoId(dep.getId());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return u;
    }

    public Usuario findOne(Usuario data) {
        try {
            //List<RecursoHumano> rrhhs = recursoHumanoService.findByPersona(data.get);
            Usuario u = repository.findOne(Example.of(data)).get();
            /*if (u != null) {
                usuarioResponsableService.verificarEstado(u);
                RecursoHumano rh = recursoHumanoService.find(new RecursoHumano(u.getUsuarioRRHH()));
                u.setRecursoHumano(rh);*/

                /*Departamento dep = departamentoService.usuariosXdepartamentos(rh.getLugarTrabajo(), rh.getTipoLugar());
                if (dep != null) {
                    u.setDepartamento(dep.getNombre());
                    u.setDepartamentoId(dep.getId());
                }
            }*/
            return u;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Usuario findById(Long id) {
        return repository.getById(id);
    }

    public List<Usuario> findAll(Usuario data) {
        return repository.findAll(Example.of(data));
    }

    public Usuario findUsername(String username) {
        return repository.findByUser(username);
    }


    public Map<String, List> find(Usuario data, Pageable pageable) {
        map = new HashMap<>();
        Page<Usuario> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING).withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Data validate(Usuario user) {
        Usuario us = repository.getById(user.getId());
        if (user.getClave().equals(us.getClave())) {
            return new Data("OK");
        }
        return null;
    }


    public Usuario usuarioXrrhh(Long rrhh) {
        Usuario u = repository.findTopByUsuarioRRHHOrderByIdDesc(rrhh);
        if (u != null) {
            u.setClave(null);
            u.setPassword(null);
        }
        return u;
    }

    public Usuario usuarioXid(Usuario data) {
        Optional<Usuario> u = repository.findOne(Example.of(data));
        if (u.isPresent()) {
            u.get().setClave(null);
            u.get().setPassword(null);
            return u.get();
        }
        return null;
    }

    public List<Usuario> usuariosXrrhh(List<RecursoHumano> rrhh) {
        List<Usuario> usuarios = new ArrayList<>();
        for (RecursoHumano r : rrhh) {
            Usuario u = repository.findTopByUsuarioRRHHOrderByIdDesc(r.getId());
            if (u != null)
                usuarios.add(u);
        }
        return usuarios;
    }


}
