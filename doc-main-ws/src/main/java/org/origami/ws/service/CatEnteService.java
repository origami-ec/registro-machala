package org.origami.ws.service;

import org.origami.ws.dto.PersonaDinardapDTO;
import org.origami.ws.entities.origami.Persona;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.models.Data;
import org.origami.ws.repository.origami.PersonaRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class CatEnteService {

    private static final Logger logger = Logger.getLogger(CatEnteService.class.getName());

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PersonaRepository enteRepository;
    @Autowired
    private ServicioDINARDAP servicioDINARDAP;


    public Persona find(Persona data) {
        Optional<Persona> dataDB = enteRepository.findOne(Example.of(data));
        if (dataDB.isEmpty()) {
            return dinardap(data);
        }
        return dataDB.orElse(null);
    }

    public Map<String, List> find(Persona data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Persona> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAll();
        if (data.getNombrerazonsocial() != null
                && !data.getNombrerazonsocial().isEmpty()
                && data.getApellidonombrecomercial() != null
                && !data.getApellidonombrecomercial().isEmpty()) {
            customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase();
            data.setNombres(data.getNombrerazonsocial());
            data.setRazonSocial(data.getNombrerazonsocial());
            data.setApellidos(data.getApellidonombrecomercial());
            data.setNombreComercial(data.getApellidonombrecomercial());
        } else if (data.getNombrerazonsocial() != null
                && !data.getNombrerazonsocial().isEmpty()) {
            customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase();
            data.setNombres(data.getNombrerazonsocial());
            data.setRazonSocial(data.getNombrerazonsocial());

        } else if (data.getApellidonombrecomercial() != null
                && !data.getApellidonombrecomercial().isEmpty()) {
            customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                    .withIgnoreCase();
            data.setApellidos(data.getApellidonombrecomercial());
            data.setNombreComercial(data.getApellidonombrecomercial());
        }
        result = enteRepository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(enteRepository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Persona registrar(Persona ente) {
        try {
            if (Utility.isNotEmptyString(ente.getIdentificacion())
                    && Utility.isNotEmptyString(ente.getNombres())) {
                ente.setFechaCre(new Date());
                return enteRepository.save(ente);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Persona actualizar(Persona ente) {
        try {
            ente.setFechaMod(new Date());
            return enteRepository.save(ente);
        } catch (Exception e) {
            return null;
        }
    }

    private Persona dinardap(Persona ente) {
        try {
            String codigoPaquete = ente.getIdentificacion().length() == 10 ? "C" : ente.getIdentificacion().length() == 13 ? "R" : "P";
            if (!codigoPaquete.equals("P")) {
                try {
                    PersonaDinardapDTO persona = servicioDINARDAP.datosDINARDAP(ente.getIdentificacion());
                    if (persona != null && Utility.isNotEmptyString(persona.getIdentificacion())
                            && Utility.isNotEmptyString(persona.getNombres())) {
                        ente.setEstado("A");
                        switch (codigoPaquete) {
                            case "R":
                                ente.setEsPersona(Boolean.FALSE);
                                ente.setRazonSocial(persona.getNombres());
                                ente.setNombreComercial(persona.getApellidos());
                                ente.setTipoIdentificacion("R");
                                break;
                            case "C":
                                ente.setEsPersona(Boolean.TRUE);
                                ente.setNombres(persona.getNombres());
                                ente.setApellidos(persona.getApellidos());
                                ente.setTipoIdentificacion("C");
                                break;
                            default:
                                break;
                        }

                        ente.setDireccion(persona.getDireccion());
                        ente.setCorreo(persona.getCorreo());
                        ente.setTelefono(persona.getTelefono());
                        ente.setFechaNacimiento(persona.getFechaNacimiento());
                        ente.setFechaExpedicion(persona.getFechaExpedicion());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ente.setEsPersona(Boolean.TRUE);
                ente.setEstado("A");
                ente.setTipoIdentificacion("P");
            }
            return registrar(ente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Persona buscarPersonaXusuario(String usuario) {
        try {
            Usuario u = usuarioService.findOne(new Usuario(usuario));
            if (u != null) {
                if (u.getRecursoHumano() != null && u.getRecursoHumano().getPersona() != null && u.getRecursoHumano().getPersona().getCedula() != null) {
                    Persona persona = enteRepository.findTopByIdentificacionOrderByIdDesc(u.getRecursoHumano().getPersona().getCedula());
                    persona.setUsuario(new Data(u.getId(), u.getUsuarioNombre()));
                    return persona;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
