package org.origami.ws.service;

import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.entities.origami.TareasHistoricas;
import org.origami.ws.repository.origami.TareasHistoricasRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TareasHistoricasService {

    @Autowired
    private TareasHistoricasRepository repository;
    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public Map<String, List> findAll(Specification<TareasHistoricas> data, Pageable pageable, Boolean terminado) {
        Map<String, List> map = new HashMap<>();
        Page<TareasHistoricas> result;
        result = repository.findAll(Specification.where(data), pageable);
        if (!Utility.isNotEmpty(result.getContent())) {
            for (TareasHistoricas th : result.getContent()) {
                if (terminado && !th.getTerminado()) {
                    result.getContent().remove(th);
                } else if (!terminado && th.getTerminado()) {
                    result.getContent().remove(th);
                }
            }
        }
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.findAll(Specification.where(data), pageable).stream().count()));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Map<String, List> findAll(TareasHistoricas data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<TareasHistoricas> result;
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, exampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, exampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public List<TareasHistoricas> findAllGroupTareas() {
        return repository.customerFindAllTareasGroupBy();
    }

    public byte[] reportTareas(List<TareasHistoricas> data) {
        return reporteService.generarPDF(Utility.armarRutaJasper("reporteUsuariosTareas", applicationProperties.getRutaReportes()), data, Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
    }
}
