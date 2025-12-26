package org.origami.ws.service;

import org.origami.ws.dto.UsuarioResponsableDto;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.origami.UsuarioResponsable;
import org.origami.ws.entities.origami.UsuarioResponsableServicio;
import org.origami.ws.entities.rrhh.RecursoHumano;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.mappers.DepartamentoMapper;
import org.origami.ws.mappers.UsuarioResponsableMapper;
import org.origami.ws.models.Data;
import org.origami.ws.repository.origami.DepartamentoRepository;
import org.origami.ws.repository.origami.UsuarioResponsableRepository;
import org.origami.ws.repository.origami.UsuarioResponsableServicioRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioResponsableService {

    @Autowired
    private UsuarioResponsableRepository repository;
    @Autowired
    private UsuarioResponsableServicioRepository usuarioResponsableServicioRepository;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RecursoHumanoService recursoHumanoService;
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private DepartamentoMapper departamentoMapper;
    @Autowired
    private UsuarioResponsableMapper usuarioResponsableMapper;
    @Autowired
    private AccionPersonalService accionPersonalService;


    public Departamento usuariosXdepartamentos(String usuario) {
        return departamentoService.usuariosXdepartamentos(usuario);
    }

    public List<UsuarioResponsableDto> consultarUsuariosXdepartamento(Long departamento) {
        List<RecursoHumano> rrhhs = recursoHumanoService.consultarRrhhXDepartamento(departamento);
        List<UsuarioResponsableDto> list = new ArrayList<>();
        for (RecursoHumano r : rrhhs) {
            String cargo = "";
            if (r.getCargo() != null) {
                if (r.getCargo().getCategoria() != null) {
                    cargo = r.getCargo().getCategoria().getDescripcion() + ": ";
                }
                cargo = cargo + r.getCargo().getNombre();
            }
            Usuario u = usuarioService.usuarioXrrhh(r.getId());
            if (u != null) {
                u.setRecursoHumano(r);
                u.setCargo(cargo);
                verificarEstado(u);
                UsuarioResponsable ur = repository.findByUsuarioAndDepartamento_Id(u.getId(), r.getLugarTrabajo());
                if (ur != null) {
                    UsuarioResponsableDto dto = usuarioResponsableMapper.toDTO(ur);
                    List<UsuarioResponsableServicio> servicios = usuarioResponsableServicioRepository.findAllByUsuarioResponsable_Id(dto.getId());
                    if (Utility.isNotEmpty(servicios)) {
                        dto.setServicios(servicios);
                    } else {
                        dto.setServicios(new ArrayList<>());
                    }
                    dto.setUsuario(u);
                    list.add(dto);
                } else {
                    list.add(new UsuarioResponsableDto(null, u, null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, departamentoMapper.toDTO(departamentoRepository.getById(departamento)), new ArrayList<>()));
                }
            }
        }
        return list;
    }

    public List<Usuario> usersXdepartamentos(Long departamento) {
        List<RecursoHumano> rrhhs = recursoHumanoService.consultarRrhhXDepartamento(departamento);
        List<Usuario> list = new ArrayList<>();
        for (RecursoHumano r : rrhhs) {
            String cargo = "";
            Usuario u = usuarioService.usuarioXrrhh(r.getId());
            if (r.getCargo() != null) {
                if (r.getCargo().getCategoria() != null) {
                    cargo = r.getCargo().getCategoria().getDescripcion() + ": ";
                }
                cargo = cargo + r.getCargo().getNombre();
            }
            if (u != null) {
                u.setCargo(cargo);
                u.setDepartamentoId(r.getLugarTrabajo());
                verificarEstado(u);
                list.add(u);
            }
        }
        return list;
    }

    public List<Usuario> usersXdepartamentosXrevisores(Long departamento) {
        List<UsuarioResponsable> list = repository.findAllByDepartamento_IdBcbgAndJefe(departamento, Boolean.TRUE);
        List<Usuario> response = new ArrayList<>();
        if (Utility.isNotEmpty(list)) {
            for (UsuarioResponsable ur : list) {
                Usuario u = usuarioService.usuarioXid(new Usuario(ur.getUsuario()));
                if (u != null) {
                    RecursoHumano rh = recursoHumanoService.find(new RecursoHumano(u.getUsuarioRRHH()));
                    String cargo = "";
                    if (rh != null) {
                        if (rh.getCargo() != null) {
                            if (rh.getCargo().getCategoria() != null) {
                                cargo = rh.getCargo().getCategoria().getDescripcion() + ": ";
                            }
                            cargo = cargo + rh.getCargo().getNombre();
                        }
                    }
                    u.setPassword(null);
                    u.setClave(null);
                    u.setCargo(cargo);
                    verificarEstado(u);
                    response.add(u);
                }
            }
        }
        return response;
    }


    public List<UsuarioResponsableDto> consultarUsuariosXdepartamentoXresponsable(Long departamento) {
        List<UsuarioResponsable> list = repository.findAllByDepartamento_IdBcbgAndResponsable(departamento, Boolean.TRUE);
        List<UsuarioResponsableDto> dtoList = new ArrayList<>();
        if (Utility.isNotEmpty(list)) {
            for (UsuarioResponsable ur : list) {
                UsuarioResponsableDto dto = usuarioResponsableMapper.toDTO(ur);
                List<UsuarioResponsableServicio> servicios = usuarioResponsableServicioRepository.findAllByUsuarioResponsable_Id(dto.getId());
                if (Utility.isNotEmpty(servicios)) {
                    dto.setServicios(servicios);
                } else {
                    dto.setServicios(new ArrayList<>());
                }
                Usuario u = usuarioService.usuarioXid(new Usuario(ur.getUsuario()));
                RecursoHumano rh = recursoHumanoService.find(new RecursoHumano(u.getUsuarioRRHH()));
                u.setPassword(null);
                u.setClave(null);
                String cargo = "";
                if (rh != null) {
                    if (rh.getCargo() != null) {
                        if (rh.getCargo().getCategoria() != null) {
                            cargo = rh.getCargo().getCategoria().getDescripcion() + ": ";
                        }
                        cargo = cargo + rh.getCargo().getNombre();
                    }
                }
                u.setCargo(cargo);
                u.setRecursoHumano(rh);
                verificarEstado(u);
                dto.setUsuario(u);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public Map<String, List> obtenerUsuariosResponsables(UsuarioResponsable data, Pageable pageable) {
        Map map = new HashMap<>();
        Page<UsuarioResponsable> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING).withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public UsuarioResponsableDto guardarUsuarioResponsable(UsuarioResponsableDto dto) {
        UsuarioResponsable usuarioResponsable = new UsuarioResponsable(dto.getId(), dto.getUsuario().getId(), dto.getObservacion(), dto.getResponsable(), dto.getDirector(), dto.getJefe(), dto.getEstado(), departamentoMapper.toEntity(dto.getDepartamento()));
        usuarioResponsable = repository.save(usuarioResponsable);
        dto.setId(usuarioResponsable.getId());
        return dto;
    }

    public UsuarioResponsable buscarUsuarioResponsable(UsuarioResponsable responsable) {
        try {
            return repository.findOne(Example.of(responsable)).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<UsuarioResponsable> obtenerResponsables(UsuarioResponsable data) {
        return repository.findAll(Example.of(data));
    }


    /**
     * Este metodo sirve para ver si el usuario esta disponible o no si se fue o no devacaciones o encargo que se yo xd
     */
    public void verificarEstado(Usuario usuario) {

        Data data = accionPersonalService.verificarUsuario(usuario);
        if (data.getId() == 0L) {
            usuario.setDisponible(Boolean.TRUE);
        } else {
            usuario.setDisponible(Boolean.FALSE);
        }
        usuario.setEstadoDisponible(data.getData());
    }


    public UsuarioResponsableServicio guardarUsuarioResponsableServicio(UsuarioResponsableServicio data) {
        Optional<UsuarioResponsableServicio> usuarioResponsableServicio = usuarioResponsableServicioRepository.findByUsuarioResponsable_IdAndServicio_Id(data.getUsuarioResponsable().getId(), data.getServicio().getId());
        if (usuarioResponsableServicio.isPresent()) {
            data.setId(usuarioResponsableServicio.get().getId());
        }
        return usuarioResponsableServicioRepository.save(data);
    }

    public UsuarioResponsableServicio eliminarResponsableServicio(UsuarioResponsableServicio data) {
        Optional<UsuarioResponsableServicio> usuarioResponsableServicio = usuarioResponsableServicioRepository.findByUsuarioResponsable_IdAndServicio_Id(data.getUsuarioResponsable().getId(), data.getServicio().getId());
        usuarioResponsableServicio.ifPresent(responsableServicio -> usuarioResponsableServicioRepository.delete(responsableServicio));
        return data;
    }
}
