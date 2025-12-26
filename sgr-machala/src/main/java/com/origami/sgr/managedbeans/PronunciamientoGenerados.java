/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpPronunciamientoJuridico;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class PronunciamientoGenerados implements Serializable {

    @Inject
    protected ServletSession ss;
    @Inject
    protected Entitymanager em;

    protected LazyModel<RegpPronunciamientoJuridico> pronunciamientos;
    protected RegpPronunciamientoJuridico pronunciamiento;
    protected RegRegistrador registrador;

    @PostConstruct
    protected void init() {
        try {
            registrador = (RegRegistrador) em.find(Querys.getJuridico);
            pronunciamientos = new LazyModel<>(RegpPronunciamientoJuridico.class, "id", "DESC");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarReporte(Long objeto) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("PronunciamientoJuridico");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", objeto);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void editarObjeto(RegpPronunciamientoJuridico objeto) {
        try {
            pronunciamiento = objeto;
            JsfUti.update("frmNota");
            JsfUti.executeJS("PF('dlgNotaDevolutiva').show();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actualizarObjeto() {
        try {
            pronunciamiento.setDetalle(pronunciamiento.getDetalle().replace("<em>", "<i>")
                    .replace("</em>", "</i>").replace("<strong>", "<b>")
                    .replace("</strong>", "</b>"));
            em.merge(pronunciamiento);
            JsfUti.messageInfo(null, "Pronunciamiento Jurídico actualizado: "
                    + pronunciamiento.getNumeroDocumento(), "");
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgNotaDevolutiva').hide();");
            pronunciamiento = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void visualizarTicket(RegpPronunciamientoJuridico objeto) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("comprobante_proforma_reingreso");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", objeto.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public LazyModel<RegpPronunciamientoJuridico> getPronunciamientos() {
        return pronunciamientos;
    }

    public void setPronunciamientos(LazyModel<RegpPronunciamientoJuridico> pronunciamientos) {
        this.pronunciamientos = pronunciamientos;
    }

    public RegpPronunciamientoJuridico getPronunciamiento() {
        return pronunciamiento;
    }

    public void setPronunciamiento(RegpPronunciamientoJuridico pronunciamiento) {
        this.pronunciamiento = pronunciamiento;
    }

}
