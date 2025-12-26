package org.origami.ws.service;

import org.origami.ws.entities.origami.Documentos;
import org.origami.ws.repository.origami.DocumentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DocumentsService {

    @Autowired
    private DocumentosRepository documentosRepository;

    public List<Documentos> consultarDocumentos(String usuario) {
        return documentosRepository.findAllByUsuarioAndAndArchivoIsNotNullOrderByIdDesc(usuario);
    }

    public byte[] consultarPDF(Long id) {
        return documentosRepository.getById(id).getArchivo();
    }

    public Documentos guardarDocumento(Documentos documentos) {
        if (documentos.getFecha() == null) {
            documentos.setFecha(new Date());
        }
        if (documentos.getEstado() == null) {
            documentos.setEstado("0");
        }
        if (documentos.getIp() == null) {
            documentos.setIp("0.0.0.0");
        }


        return documentosRepository.save(documentos);
    }


}
