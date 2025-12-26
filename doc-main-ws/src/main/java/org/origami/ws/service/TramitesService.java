package org.origami.ws.service;

import org.origami.ws.entities.origami.Tramites;
import org.origami.ws.repository.origami.TramitesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TramitesService {
    @Autowired
    private TramitesRepository repository;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Boolean encontrado = false;

    public Map<String, List> findAll(Tramites data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Tramites> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Tramites findAllByUsuarioAndTramite(String usuario, Long idTramite, String fechaDesde, String fechaHasta, String tipo) {
        try {
            List<Tramites> terminados = new ArrayList<>();
            List<Tramites> pendientes = new ArrayList<>();
            switch (tipo) {
                case "SERVICIO":
                    terminados = repository
                            .findAllCustomerByIdServicio_IdAndParticipantsContainsAndStartTimeBetween(idTramite,
                                    usuario, format.parse(fechaDesde + " 00:00:00.000"),
                                    format.parse(fechaHasta + " 23:59:59.000"), true).orElse(new ArrayList<>());
                    pendientes = repository
                            .findAllCustomerByIdServicio_IdAndParticipantsContainsAndStartTimeBetween(idTramite,
                                    usuario, format.parse(fechaDesde + " 00:00:00.000"),
                                    format.parse(fechaHasta + " 23:59:59.000"), false).orElse(new ArrayList<>());
                    break;
                case "TRAMITE":
                    terminados = repository
                            .findAllCustomerByIdTipoTramite_IdAndParticipantsContainsAndStartTimeBetween(idTramite,
                                    usuario, format.parse(fechaDesde + " 00:00:00.000"),
                                    format.parse(fechaHasta + " 23:59:59.000"), true).orElse(new ArrayList<>());
                    pendientes = repository
                            .findAllCustomerByIdTipoTramite_IdAndParticipantsContainsAndStartTimeBetween(idTramite,
                                    usuario, format.parse(fechaDesde + " 00:00:00.000"),
                                    format.parse(fechaHasta + " 23:59:59.000"), false).orElse(new ArrayList<>());
                    break;
            }
            Tramites data = new Tramites();
            data.setParticipants(usuario);
            data.setPendientes(0L);
            data.setTerminados(0L);
            for (Tramites t : terminados) {
                data.setTerminados(data.getTerminados() + (t.getValue() != null ? t.getValue() : 0L));
            }
            for (Tramites p : pendientes) {
                data.setPendientes(data.getPendientes() + (p.getValue() != null ? p.getValue() : 0L));
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tramites> findAll(String fechaDesde, String fechaHasta) {
        try {
            List<Tramites> terminados = repository
                    .findCustomerTramitesTerminados(format.parse(fechaDesde + " 00:00:00.000"),
                            format.parse(fechaHasta + " 23:59:59.000"));
            List<Tramites> pendientes = repository.findCustomerTramitesPendientes(format.parse(fechaDesde + " 00:00:00.000"),
                    format.parse(fechaHasta + " 23:59:59.000"));
            return customerTramites(terminados, pendientes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Tramites> customerTramites(List<Tramites> terminados, List<Tramites> pendientes) {
        List<Tramites> data = new ArrayList<>();
        if (!terminados.isEmpty() && !pendientes.isEmpty()) {
            terminados.forEach(t -> {
                Tramites tramites = new Tramites();
                tramites.setIdTipoTramite(t.getIdTipoTramite());
                tramites.setTerminados(t.getValue() != null ? t.getValue() : 0L);
                tramites.setPendientes(0L);
                pendientes.forEach(p -> {
                    if (t.getIdTipoTramite().getId().equals(p.getIdTipoTramite().getId())) {
                        tramites.setPendientes(p.getValue() != null ? p.getValue() : 0L);
                    }
                });
                data.add(tramites);
            });
            pendientes.forEach(p -> {
                Tramites tramites = new Tramites();
                tramites.setIdTipoTramite(p.getIdTipoTramite());
                tramites.setPendientes(p.getValue() != null ? p.getValue() : 0L);
                tramites.setTerminados(0L);
                for (Tramites d : data) {
                    encontrado = false;
                    if (p.getIdTipoTramite().getId().equals(d.getIdTipoTramite().getId())) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    data.add(tramites);
                }
            });
        } else if (terminados.isEmpty() && !pendientes.isEmpty()) {
            pendientes.forEach(p -> {
                p.setPendientes(p.getValue());
                p.setTerminados(0L);
                data.add(p);
            });
        } else if (!terminados.isEmpty()) {
            terminados.forEach(t -> {
                t.setTerminados(t.getValue());
                t.setPendientes(0L);
                data.add(t);
            });
        }
        return data;
    }
}
