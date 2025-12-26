package org.origami.ws.service;

import org.origami.ws.entities.origami.RolUsuario;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.repository.origami.RolUsuarioRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RolUsuarioService {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;
    @Autowired
    private UsuarioService usuarioService;

    public RolUsuario find(RolUsuario data) {
        List<RolUsuario> dataBD = rolUsuarioRepository.findAll(Example.of(data));
        if (!dataBD.isEmpty()) {
            return dataBD.get(0);
        } else {
            return null;
        }
    }

    public Map<String, List> find(RolUsuario data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<RolUsuario> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = rolUsuarioRepository.findAll(Example.of(data), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(rolUsuarioRepository.count(Example.of(data))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public List<RolUsuario> findAll(RolUsuario data) {
        return rolUsuarioRepository.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Usuario> findAllUsuariosByRol(RolUsuario data) {
        List<RolUsuario> dataBD = rolUsuarioRepository.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "usuario.ente.apellidos"));

        List<Usuario> usuarios = new ArrayList<>();
        if (Utility.isNotEmpty(dataBD)) {
            for (RolUsuario rolUsuario : dataBD) {
                usuarios.add(usuarioService.findOne(new Usuario(rolUsuario.getUsuario(), null, null, null)));
            }
        } else {
            System.out.printf("Revise que un usuario tenga el rol: " + data.getRol().getNombre());
        }
        usuarios = usuarios.stream().distinct().collect(Collectors.toList());
        return usuarios;
    }

    public List<RolUsuario> saveAll(List<RolUsuario> rolUsuarios) {
        List<RolUsuario> rolUsuarioDB = new ArrayList<>();
        for (RolUsuario r : rolUsuarios) {
            if (!existeRolUsuario(r.getUsuario(), r.getRol().getId())) {
                rolUsuarioDB.add(save(r));
            }
        }
        return rolUsuarioDB;
    }

    public RolUsuario save(RolUsuario rolUsuario) {
        return rolUsuarioRepository.save(rolUsuario);
    }

    public void deleteAll(List<RolUsuario> rolUsuarios) {
        for (RolUsuario ru : rolUsuarios) {
            RolUsuario rDB = rolUsuarioRepository.findByUsuarioAndRol_Id(ru.getUsuario(), ru.getRol().getId());
            if (rDB != null) {
                delete(rDB);
            }
        }
    }

    public void delete(RolUsuario rolUsuario) {
        rolUsuarioRepository.delete(rolUsuario);
    }

    public Boolean existeRolUsuario(Long idUsuario, Long idRol) {
        return rolUsuarioRepository.findByUsuarioAndRol_Id(idUsuario, idRol) != null;
    }

}
