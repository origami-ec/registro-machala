/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.Contenido;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Ricardo
 */
@ViewScoped
@Named
public class ContenidoMB implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private UserSession session;
    private Contenido contenido;
    private LazyModelWS<Contenido> contenidos;
    private String imagen;

    @PostConstruct
    public void init() {
        loadModel();
    }

    public void loadModel() {
        contenidos = new LazyModelWS<>(SisVars.wsPublic + "contenidos/find?estado=true&sort=orden,asc", Contenido[].class, session.getToken());
        loadData();
    }

    private void loadData() {
        contenido = new Contenido();
    }

    public void edit(Contenido contenido) {
        this.contenido = contenido;
        JsfUti.update("formData");
        JsfUti.executeJS("PF('sidebarData').show()");
    }

    public void save() {
        if (validar()) {
            Contenido cDB = (Contenido) service.methodPOST(contenido, SisVars.wsPublic + "save/content", Contenido.class);
            if (cDB != null && cDB.getId() != null) {
                loadData();
                JsfUti.messageInfo(null, "", "Dato guardado correctamente");
                JsfUti.update("formMain");
                JsfUti.update("formData");
                JsfUti.executeJS("PF('sidebarData').hide()");
            } else {
                JsfUti.messageInfo(null, "", "Ocurrío un error, intenten nuevamente");
            }
        }
    }

    public void delete(Contenido c) {
        c.setEstado(Boolean.FALSE);
        c = (Contenido) service.methodPUT(c, SisVars.wsPublic + "update/content", Contenido.class);
        if (c != null && c.getId() != null) {
            JsfUti.messageInfo(null, "", "Eliminado correctamente");
        } else {
            JsfUti.messageInfo(null, "", "Ocurrio un error, intente nuevamente");
        }
    }

    public Boolean validar() {
        if (contenido.getNombre() == null) {
            JsfUti.messageError(null, "", "Ingrese un nombre");
            return false;
        }
        if (contenido.getOrden() == null) {
            JsfUti.messageError(null, "", "Especifique el orden");
            return false;
        }
        return true;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            String prefix = FilenameUtils.getBaseName(imagen);
            String suffix = FilenameUtils.getExtension(imagen);
            contenido.setArchivo(prefix + "." + suffix);
            contenido.setTipoFormato(suffix);
            contenido.setByteArchivo(event.getFile().getContent());
//            contenido.setImagen64(Base64.getEncoder().encodeToString(event.getFile().getContent()));
        } catch (Exception ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
        }
    }

//<editor-fold defaultstate="collapsed" desc="getters and setter">
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public LazyModelWS<Contenido> getContenidos() {
        return contenidos;
    }

    public void setContenidos(LazyModelWS<Contenido> contenidos) {
        this.contenidos = contenidos;
    }

//</editor-fold>
}
