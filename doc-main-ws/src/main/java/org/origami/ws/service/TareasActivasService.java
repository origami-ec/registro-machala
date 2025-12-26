package org.origami.ws.service;

import org.origami.ws.entities.origami.TareasActivas;
import org.origami.ws.models.Data;
import org.origami.ws.repository.origami.HistoricoTramiteRepository;
import org.origami.ws.repository.origami.TareasActivasRepository;
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
public class TareasActivasService {

    @Autowired
    private TareasActivasRepository repository;

    @Autowired
    private HistoricoTramiteRepository historicoTramiteRepository;

    public TareasActivas find(TareasActivas data) {
        return repository.findOne(Example.of(data)).get();
    }

    public List<TareasActivas> findAllTareasActivas(){
        return repository.findAll();
    }

    public Data cantidadTramiteUser(Data data) {
        return new Data(repository.countByUser(data.getData()));
    }

    public List<TareasActivas> findAll(TareasActivas data) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return repository.findAll(Example.of(data, matcher));
    }


    public Map<String, List> findPagingAndSorting(TareasActivas data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<TareasActivas> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAll();
        customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Map<String, List> findPagingAndSorting(Specification<TareasActivas> data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<TareasActivas> result;
        result = repository.findAll(Specification.where(data), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.findAll(Specification.where(data)).stream().count()));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public static String mailHtmlFinTramite(String descripcion, Long numTramite, String nombreUser) {
        return "<html lang =\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"text-align: center;\"><strong>" + descripcion + "</strong></p>\n"
                + "<p style=\"text-align: center;\"><strong>Tramite N°: " + numTramite + "</strong></p>\n"
                + "<p style=\"text-align: left;\">"
                + "Estimado(a) <strong>" + nombreUser + "</strong>, el trámite "
                + " número: <strong>" + numTramite + "</strong>., le comunicamos que dicho tramite ha pasado el tiempo de entrega\n"
                + "</p>\n"
                + "<p style=\"text-align: left;\">Este correo fue enviado de forma automática y no requiere respuesta.</p>"
                + "<p style=\"text-align: left;\">&nbsp;</p>"
                + "</body>\n"
                + "</html>";
    }
}
