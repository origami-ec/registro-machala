package org.origami.ws.service;

import org.origami.ws.entities.origami.SolicitudDocumentos;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.repository.origami.ServiciosDepartamentoItemsRequisitoRepository;
import org.origami.ws.repository.origami.SolicitudDocumentosRespository;
import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.config.ServiceRest;
import org.origami.ws.dto.SolicitudDocumentosDTO;
import org.origami.ws.mappers.SolicitudDocumentosMapper;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudDocumentosService {

    @Autowired
    private SolicitudDocumentosRespository respository;
    @Autowired
    private SolicitudDocumentosMapper mapper;
    @Autowired
    private ServiceRest remoteService;
    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private ServiciosDepartamentoItemsRequisitoRepository serviciosRequisitoRepository;
    @Autowired
    private UsuarioService usuarioService;

    public List<SolicitudDocumentosDTO> find(SolicitudDocumentos data) {
        List<SolicitudDocumentosDTO>  docs = mapper.toDTO(respository.findAll(Example.of(data)));
        if(Utility.isNotEmpty(docs)){
            for (SolicitudDocumentosDTO documentosDTO : docs){

            }
        }
        return docs;
    }

    public SolicitudDocumentosDTO findOne(SolicitudDocumentos data) {
        SolicitudDocumentos d = respository.findOne(Example.of(data)).orElse(new SolicitudDocumentos());
        if (d != null) {
            Usuario u = usuarioService.findOne(new Usuario(d.getSolicitud().getUsuarioCreacion()));
            d.getSolicitud().setUsuarioIngreso(u);
            return mapper.toDTO(d);
        }
        return new SolicitudDocumentosDTO();

    }

    public SolicitudDocumentos guardar(SolicitudDocumentos documentos) {
        try {
//            Gson gson = new Gson();
//            SolicitudDocumentos sd = respository.save(documentos);
            /*DetalleQR detalleQR;
            if (sd.getRequisito() != null && sd.getRequisito().getId() != null && sd.getTieneQr() != null && !sd.getTieneQr()) {
                ServiciosDepartamentoRequisitos sdr = serviciosRequisitoRepository.findById(sd.getRequisito().getId()).get();
                if (sdr.getId() != null && sdr.getServiciosDepartamento() != null
                        && (sdr.getServiciosDepartamento().getAbreviatura().equals("EMPERCH")
                        || sdr.getServiciosDepartamento().getAbreviatura().equals("EMPERCU"))) {
                    detalleQR = new DetalleQR("", "PERMISO_" + sd.getId(), sd.getRutaArchivo(), sd.getNombreArchivo());
                    detalleQR = (DetalleQR) remoteService.methodPOST(properties.getUrlMedia() + "generar/qr", gson.toJson(detalleQR), DetalleQR.class, "", "");
                    sd.setRutaArchivo(detalleQR.getArchivo());
                    sd.setTieneQr(Boolean.TRUE);
                }
            }*/
            return respository.save(documentos);
        } catch (Exception e) {
            System.out.println("//Exception " + e.getMessage());
            return null;
        }
    }

    public List<SolicitudDocumentos> guardar(List<SolicitudDocumentos> documentos) {
        return respository.saveAll(documentos);
    }

    public void eliminar(SolicitudDocumentos documentos) {
        respository.delete(documentos);
    }

    public List<SolicitudDocumentos> actualizar(List<SolicitudDocumentos> documentos) {
        return respository.saveAll(documentos);
    }

    public SolicitudDocumentos findByRequisitoANDSolicitudTramiteId(String numTramite, Long idRequisito) {
        return respository.findByRequisito_Solicitud_tramite_id(numTramite, idRequisito);
    }

    public SolicitudDocumentos updateDocRequisitos(SolicitudDocumentos solicitudDocumentos) {
        solicitudDocumentos = guardar(solicitudDocumentos);
        return solicitudDocumentos;
    }
}
