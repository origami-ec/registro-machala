/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans.component;

import org.bcbg.entities.Imagenes;
import org.bcbg.entities.User;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.documental.models.UsuarioDocs;
import org.primefaces.PrimeFaces;
import org.primefaces.extensions.event.ImageAreaSelectEvent;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class DlgFirmaElectronicaFecha implements Serializable {

    @Inject
    private AppServices appServices;

    @Inject
    private UserSession us;
    @Inject
    private ServletSession ss;

    private FirmaElectronicaModel firmaElectronica;
    private String clave;
    // //SIRVE PARA CONDICIONAR SI YA LA CLAVE INGRESADA ESTA VALIDADA PARA QUE SE PROCEDA A MOSTRAR LAS URL DE LAS IMAGENES DEL PDF
    private Boolean imagenPDF;
    private List<Imagenes> imagenesPdfs;
    private Boolean existenImagenes;

    @PostConstruct
    protected void initView() {
        imagenPDF = Boolean.valueOf(ss.getParametros().get("imagenPDF").toString());
        if (imagenPDF == null) {
            imagenPDF = Boolean.FALSE;
        }
        firmaElectronica = (FirmaElectronicaModel) ss.getParametros().get("firmaElectronicaModel");
        firmaElectronica.getFirmaElectronica().setUsuario(us.getUsuarioDocs());

        clave = "";
        imagenesPdfs = new ArrayList<>();
        existenImagenes = Boolean.FALSE;

    }

    /**
     * En teoria la firma esta validada en el sistema y si deberia dejar firmar
     * =V SI RETORNA NULL TONCES NO SE FIRMO
     */
    public void validarFirmarDocumento() {
        try {
            if (validarFirma()) {
                firmaElectronica.getFirmaElectronica().setClave(clave);
                FirmaElectronica archivoGenerado = appServices.firmarElectronicamente(firmaElectronica.getFirmaElectronica());
                if (archivoGenerado != null) {
                    JsfUti.messageInfo(null, Messages.transaccionOK, "");
                } else {
                    JsfUti.messageInfo(null, "No se pudo firmar el documento", "");
                }
                PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *
     * ESTE METODO SIRVE PARA VALIDAR LA FIRMA ELECTRONICA Y VER SI PROCEDE A
     * QUE SE GENEREN LAS URLS (IMAGENES) DEL PDF SUBIDO Y ASI PODER OBTENER LA
     * POSICION DONDE SERÁ FIRMADO EL PDF
     */
    public void validarFirmaImagenPDF() {
        if (validarFirma()) {
            imagenesPdfs = appServices.obtenerImagenesDesdePDF(firmaElectronica.getFirmaElectronica().getArchivoFirmar(), firmaElectronica.getFirmaElectronica().getNumeroPagina());
            if (Utils.isNotEmpty(imagenesPdfs)) {
                existenImagenes = Boolean.TRUE;
            }
        }
    }

    /**
     * Obtiene la posicion de la imagen en dond se necesita que la firma sea
     * puesta
     *
     * @param e
     */
    public void selectEndListener(ImageAreaSelectEvent e) {

        //if (e.getWidth() == 150 && e.getHeight() == 50) {
        String pagina = e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", "");
        firmaElectronica.getFirmaElectronica().setClave(clave);
        //firmaElectronica.getFirmaElectronica().setNumeroPagina(Integer.valueOf(pagina) + 1); //YA VIENE SETEADO DESDE QUE SE LLAMA AL DLG
        firmaElectronica.getFirmaElectronica().setPosicionX1("" + (e.getX1()));
        firmaElectronica.getFirmaElectronica().setPosicionX2("" + (e.getX2()));
        firmaElectronica.getFirmaElectronica().setPosicionY1(((e.getImgHeight() - e.getY1())) + "");
        firmaElectronica.getFirmaElectronica().setPosicionY2(((e.getImgHeight() - e.getY2())) + "");
        Date fecha = (Date) ss.getParametros().get("fechaFirma");
        firmaElectronica.setFechaFirmar(fecha);
        firmaElectronica.setFechaCreacion(new Date());
        FirmaElectronica archivoGenerado = appServices.firmarElectronicamente(firmaElectronica);
        PrimeFaces.current().dialog().closeDynamic(archivoGenerado);
        /*  } else {
            JsfUti.messageError(null, "Debe seleccionar el tamaño completo del rectángulo para la firma electrónica", "");
        }*/
    }

    private Boolean validarFirma() {
        try {
            if (!clave.isEmpty()) {
                if (us.getFirmaElectronica() != null) {
                    if (!us.getFirmaElectronica().getFirmaCaducada()) {
                        String archivo = us.getFirmaElectronica().getArchivo();
                        FirmaElectronica firmaValidada = appServices.validarFirmaElectronica(archivo, clave);
                        if (firmaValidada != null && firmaValidada.getUid() != null) {
                            return Boolean.TRUE;
                        } else {
                            JsfUti.messageError(null, "Clave incorrecta intente nuevamente", "");
                            return Boolean.FALSE;
                        }
                    } else {
                        JsfUti.messageError(null, "Su firma electrónica esta caducada", "");
                        return Boolean.FALSE;
                    }
                } else {
                    JsfUti.messageError(null, "Suba su firma electrónica, primero", "");
                    return Boolean.FALSE;
                }
            } else {
                JsfUti.messageError(null, "Ingrese su clave ", "");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getImagenPDF() {
        return imagenPDF;
    }

    public void setImagenPDF(Boolean imagenPDF) {
        this.imagenPDF = imagenPDF;
    }

    public List<Imagenes> getImagenesPdfs() {
        return imagenesPdfs;
    }

    public void setImagenesPdfs(List<Imagenes> imagenesPdfs) {
        this.imagenesPdfs = imagenesPdfs;
    }

    public Boolean getExistenImagenes() {
        return existenImagenes;
    }

    public void setExistenImagenes(Boolean existenImagenes) {
        this.existenImagenes = existenImagenes;
    }

    public FirmaElectronicaModel getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronicaModel firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

}
