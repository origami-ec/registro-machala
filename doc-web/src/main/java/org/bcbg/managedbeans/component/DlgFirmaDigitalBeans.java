/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans.component;

import org.bcbg.entities.Imagenes;
import org.bcbg.entities.User;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.bcbg.documental.models.UsuarioDocs;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.ImageAreaSelectEvent;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "dlgFirmaDigitalBeans")
@ViewScoped
public class DlgFirmaDigitalBeans implements Serializable {

    @Inject
    private AppServices appServices;

    @Inject
    private UserSession us;

    @Inject
    private ServletSession ss;

    private FirmaElectronica firmaElectronica;
    private Boolean imagenPDF;
    private List<Imagenes> imagenesPdfs;
    private Boolean existenImagenes;

    @PostConstruct
    public void init() {
        imagenPDF = Boolean.valueOf(ss.getParametros().get("imagenPDF").toString());
        if (imagenPDF == null) {
            imagenPDF = Boolean.FALSE;
        }
        firmaElectronica = (FirmaElectronica) ss.getParametros().get("firmaElectronica");
        firmaElectronica.setUsuario(us.getUsuarioDocs());
        imagenesPdfs = new ArrayList<>();
        existenImagenes = Boolean.FALSE;
    }

    public void obtenerPdfEnImages() {
        imagenesPdfs = appServices.obtenerImagenesDesdePDF(firmaElectronica.getArchivoFirmar(), 1);
        if (Utils.isNotEmpty(imagenesPdfs)) {
            existenImagenes = Boolean.TRUE;
        }
    }

    /**
     * Obtiene la posicion de la imagen en dond se necesita que la firma sea
     * puesta
     *
     * @param e
     */
    public void selectEndListener(ImageAreaSelectEvent e) {
        if (e.getWidth() == 150 && e.getHeight() == 50) {
            String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
            firmaElectronica.setNumeroPagina(Integer.valueOf(pagina) + 1);
            firmaElectronica.setPosicionX1("" + (e.getX1()));
            firmaElectronica.setPosicionX2("" + (e.getX2()));
            firmaElectronica.setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
            firmaElectronica.setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
            FirmaElectronica archivoGenerado = appServices.firmarDigitalmente(firmaElectronica);
            if (archivoGenerado != null) {
                JsfUti.messageInfo(null, "Archivo firmado correctamente.", "");
            } else {
                JsfUti.messageError(null, "No se pudo firmar el archivo.", "");
            }
            PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
        } else {
            JsfUti.messageError(null, "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica", "");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setteres">
    public List<Imagenes> getImagenesPdfs() {
        return imagenesPdfs;
    }

    public void setImagenesPdfs(List<Imagenes> imagenesPdfs) {
        this.imagenesPdfs = imagenesPdfs;
    }

    public Boolean getImagenPDF() {
        return imagenPDF;
    }

    public void setImagenPDF(Boolean imagenPDF) {
        this.imagenPDF = imagenPDF;
    }

    public Boolean getExistenImagenes() {
        return existenImagenes;
    }

    public void setExistenImagenes(Boolean existenImagenes) {
        this.existenImagenes = existenImagenes;
    }

//</editor-fold>
}
