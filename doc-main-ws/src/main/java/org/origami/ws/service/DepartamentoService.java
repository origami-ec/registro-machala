package org.origami.ws.service;

import org.origami.ws.entities.origami.CatalogoItem;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.rrhh.RecursoHumano;
import org.origami.ws.models.Data;
import org.origami.ws.repository.origami.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private SincronizacionServices sincronizacionServices;
    @Autowired
    private RecursoHumanoService recursoHumanoService;

    public Data sincronizarDeps() {
        sincronizacionServices.actualizarDatosDepartamento();
        return new Data(0L, "OK");
    }

    public Departamento usuariosXdepartamentos(String usuario) {
        RecursoHumano rh = recursoHumanoService.findByPersona(usuario);
        if (rh!=null && rh.getLugarTrabajo() != null && rh.getTipoLugar() != null) {
            Optional<Departamento> depOp = departamentoRepository.findByIdBcbgAndTipo(rh.getLugarTrabajo(), rh.getTipoLugar());
            if (depOp.isPresent()) {
                return depOp.get();
            }
        }
        return null;
    }

    public Departamento usuariosXdepartamentos(Long lugarTrabajo, Long tipoLugar) {

        Optional<Departamento> depOp = departamentoRepository.findByIdBcbgAndTipo(lugarTrabajo, tipoLugar);
        if (depOp.isPresent()) {
            return depOp.get();
        }
        return null;
    }

    public List<Departamento> findAllDepartamentoByServiciosDepartamento() {
        return departamentoRepository.findAllCustomDepartamentoByServicioDepartamento();
    }

    public List<Departamento> findAllDepartamento(Departamento data) {
        return departamentoRepository.findAll(Example.of(data), Sort.by(Sort.Direction.ASC, "nombre"));
    }

    public List<Departamento> findAllDepartamento() {
        return departamentoRepository.findAllDepartamentos();
    }

    public List<Departamento> findAllDepartamentoWithUser() {
        return departamentoRepository.findAllDepartamentosWithUser();
    }

    public Departamento findAllDireccion(Long idDepartamento) {
        try {
            Departamento ddb = find(new Departamento(idDepartamento));
            if (ddb != null && ddb.getId() != null) {
                return ddb;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


    public Departamento find(Departamento data) {
        return departamentoRepository.findOne(Example.of(data)).orElse(null);
    }

    public Map<String, List> find(Departamento data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Departamento> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = departamentoRepository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(departamentoRepository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Departamento save(Departamento data) {
        return departamentoRepository.save(data);
    }

    public Departamento getDepartamentoNombre(String data) {

        List<Departamento> result = departamentoRepository.findByCodigoAndEstado(data, Boolean.TRUE);
        return result.get(0);
    }

    public List<CatalogoItem> tipoUnidades(String nombre) {
        List<CatalogoItem> result = departamentoRepository.findByTiposUnidades(nombre, "A");
        return result;
    }


    public List<Departamento> getPadresDepartamentos() {
        List<Departamento> result = departamentoRepository.getDepartamentoPadre();
        return result;
    }

    public List<Departamento> getHijosDepartamentos(Long padre) {
        List<Departamento> result = departamentoRepository.getDeartamentoHijos();
        return result;
    }

    public List<Departamento> getHijosDepartamentosByEstado(Long padre) {
        List<Departamento> result = departamentoRepository.getDeartamentoHijosByEstado();
        return result;
    }
}
