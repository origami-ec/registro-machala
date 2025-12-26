package org.bcbg.documental.managedbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexCampoDto;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class MisDocumentosMB extends BpmManageBeanBaseRoot implements Serializable {

    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private List<Indexacion> indices;
    private List<ArchivoDocs> archivosBusqueda;
    private ArchivoDocs archivo;
    private Indexacion indice;
    private String numTramite;

    @PostConstruct
    public void init() {
        try {
            indice = new Indexacion();
            indices = documentalService.getIndices();
            lazyArchivos = new LazyModelWS<>(SisVars.wsDocs + "misDocumentos?usuario.referenceId=" + session.getUserId(), session.getToken());
            lazyArchivos.setEntitiArray(ArchivoDocs[].class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarArchivo(String archivoId) {
        ArchivoDocs temp = documentalService.eliminarArchivo(new ArchivoDocs(archivoId));
        if (temp != null) {
            JsfUti.messageInfo(null, "Archivo eliminado con éxito", "");
        } else {
            JsfUti.messageError(null, "Intente nuevamente", "");
        }
        init();
    }

    public void consultarArchivo(String archivoId) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.agregarParametro("archivoId", archivoId);
        JsfUti.redirectFaces("procesos/documentos/documento.xhtml");
    }

    public void dlgConvertirArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
        JsfUti.executeJS("PF('dlgConversion').show()");
        JsfUti.update("frmConversion");
    }

    public void convertirArchivo() {
        if (Utils.isNotEmptyString(archivo.getConvertir())) {
            archivo = documentalService.convertirArchivo(new ArchivoDocs(archivo.getId(), archivo.getConvertir()));
            if (archivo != null && Utils.isNotEmptyString(archivo.getRutaConvertido())) {
                getDownloadFile(archivo.getRutaConvertido());
            } else {
                JsfUti.messageError(null, "Intente nuevamente", "");
            }
        } else {
            JsfUti.messageError(null, "Debe escoger el tipo de conversión", "");
        }

    }

    public void visualizarDocumento(ArchivoDocs docs) {
        if (!docs.getFormato().startsWith("IMG")) {
            //this.getPDF(docs.getRuta());
            this.getPDF(docs.getNombre());
        } else {
            JsfUti.redirectNewTab(Utils.getUrlViewImage(docs.getRuta()));
        }
    }

    public void buscarArchivos() {
        if (indice == null || indice.get_id() == null) {
            JsfUti.messageError(null, Messages.escojaIndice, "");
            return;
        }

        archivosBusqueda = new ArrayList<>();
        ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
        archivoIndex.setNumTramite(numTramite.trim());
        archivoIndex.setDetalleDocumento(indice.getDescripcionArchivo().trim());
        archivoIndex.setEstado(Boolean.TRUE);
        archivoIndex.setTipoIndexacion(indice.getDescripcion());
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        for (IndexacionCampo ic : indice.getCampos()) {
            detalles.add(new ArchivoIndexCampoDto(ic.getTipoDato(), ic.getDescripcion(), ic.getCategorias(), ic.getDetalle(), ic.getObligatorio()));
        }
        archivoIndex.setDetalles(detalles);

        archivosBusqueda = documentalService.busquedaAvanzada(archivoIndex);
        if (archivosBusqueda == null) {
            JsfUti.messageError(null, "No se encontraron datos", "");
            archivosBusqueda = new ArrayList<>();
        }
    }

    public void onRowEdit(RowEditEvent<IndexacionCampo> event) {
    }

    public void onRowCancel(RowEditEvent<IndexacionCampo> event) {
    }

    public LazyModelWS<ArchivoDocs> getLazyArchivos() {
        return lazyArchivos;
    }

    public void setLazyArchivos(LazyModelWS<ArchivoDocs> lazyArchivos) {
        this.lazyArchivos = lazyArchivos;
    }

    public List<Indexacion> getIndices() {
        return indices;
    }

    public void setIndices(List<Indexacion> indices) {
        this.indices = indices;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Indexacion getIndice() {
        return indice;
    }

    public void setIndice(Indexacion indice) {
        this.indice = indice;
    }

    public List<ArchivoDocs> getArchivosBusqueda() {
        return archivosBusqueda;
    }

    public void setArchivosBusqueda(List<ArchivoDocs> archivosBusqueda) {
        this.archivosBusqueda = archivosBusqueda;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

}
