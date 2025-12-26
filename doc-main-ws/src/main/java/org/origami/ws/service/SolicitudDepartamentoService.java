package org.origami.ws.service;

import org.origami.ws.entities.origami.SolicitudDepartamento;
import org.origami.ws.entities.origami.SolicitudDocumentos;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.repository.origami.ObservacionRepository;
import org.origami.ws.repository.origami.SolicitudDepartamentoRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudDepartamentoService {

    @Autowired
    private SolicitudDepartamentoRepository repository;
    @Autowired
    private SolicitudServiciosService serviciosService;
    @Autowired
    private SolicitudDocumentosService docsService;
    @Autowired
    private ObservacionRepository observacionRepository;


    public List<SolicitudDepartamento> find(SolicitudDepartamento data) {
        List<SolicitudDepartamento>  list = repository.findAll(Example.of(data));
        return list;
    }

    public SolicitudDepartamento registrar(SolicitudDepartamento data) {
        try {
            Boolean estado = data.getEstado();
            if (data.getSolicitud().getId() == null) {
                List<SolicitudDocumentos> documentos = data.getSolicitud().getDocumentos();
                SolicitudServicios servicios = serviciosService.registrar(data.getSolicitud());
                if (Utility.isNotEmpty(documentos)) {
                    documentos.stream().forEach((docs) -> docs.setSolicitud(servicios));
                    docsService.guardar(documentos);
                }
                servicios.setDocumentos(null);
                data.setSolicitud(servicios);
            }
            if(estado == null){
                estado = true;
                System.out.println("entro");
            } data.setEstado(estado);
            System.out.println("solciitud dpto:"+data.toString());
            return repository.save(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SolicitudDepartamento> actualizarList(List<SolicitudDepartamento> solicitudDepartamentos) {
        return repository.saveAll(solicitudDepartamentos);
    }

    public SolicitudDepartamento actualizar(SolicitudDepartamento solicitudDepartamentos) {
        return repository.save(solicitudDepartamentos);
    }


}
