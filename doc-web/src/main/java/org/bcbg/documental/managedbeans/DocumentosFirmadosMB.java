package org.bcbg.documental.managedbeans;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.DocumentoFirmado;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.config.SisVars;

@Named
@ViewScoped
public class DocumentosFirmadosMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<DocumentoFirmado> lazy;

    @PostConstruct
    public void init() {
        try {
            lazy = new LazyModelWS<>(SisVars.wsDocs + "documentoFirmados", session.getToken());
            lazy.setEntitiArray(DocumentoFirmado[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consultarArchivo(String archivoId) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("archivoId", archivoId);
        // JsfUti.redirectFaces("procesos/documentos/documento.xhtml");
    }

    public void visualizarDocumento(DocumentoFirmado docs) {
        getPDF(docs.getArchivoFirmado());
    }

    public LazyModelWS<DocumentoFirmado> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModelWS<DocumentoFirmado> lazy) {
        this.lazy = lazy;
    }

}
