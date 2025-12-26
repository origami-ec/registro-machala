/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Juan Carlos
 */
@Named
@ViewScoped
public class NotasDevolutivasRpIngresadas extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private AsynchronousService as;

    protected Boolean permitido = false, admin = false;
    private LazyModel<RegpNotaDevolutiva> notaDevolutivas;
    private RegpNotaDevolutiva notaDevolutiva;
    private RegRegistrador registrador;

    @PostConstruct
    protected void init() {
        try {
            registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
            notaDevolutivas = new LazyModel<>(RegpNotaDevolutiva.class, "id", "DESC");
            this.validaRoles();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void validaRoles() {
        admin = session.getRoles().contains(1L);
        permitido = session.getRoles().contains(1L) || session.getRoles().contains(92L) || session.getRoles().contains(97L);
    }

    public void generarDevolutiva(RegpNotaDevolutiva nd) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", nd.getId());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("USUARIO", session.getName_user());
            if (nd.getAsunto().toUpperCase().contains("NEGATIVA")) {
                ss.setNombreReporte("RazonNegativa");
            } else {
                ss.setNombreReporte("NotaDevolutiva");
            }
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void imprimirNotaDevolutiva(RegpNotaDevolutiva nd) {
        try {
            if (nd.getDocumento() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento con firma electrónica", "");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + nd.getDocumento()
                        + "&name=" + nd.getNumNotaDevolutiva() + ".pdf&tipo=3&content=application/pdf");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void editarNotaDevolutiva(RegpNotaDevolutiva devolutiva) {
        notaDevolutiva = devolutiva;
        JsfUti.update("frmNota");
        JsfUti.executeJS("PF('dlgNotaDevolutiva').show();");
    }

    public void actualizarDetalleNotaDevolutiva() {
        try {
            notaDevolutiva.setDetalle(notaDevolutiva.getDetalle().replace("<em>", "<i>").replace("</em>", "</i>")
                    .replace("<strong>", "<b>").replace("</strong>", "</b>"));
            manager.merge(notaDevolutiva);
            JsfUti.messageInfo(null, "Nota Devolutiva Actualizada: " + notaDevolutiva.getNumNotaDevolutiva(), "");
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgNotaDevolutiva').hide();");
            notaDevolutiva = null;
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void visualizarTicket(RegpNotaDevolutiva rnd) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setImprimir(true);
            ss.setNombreReporte("comprobante_proforma_reingreso");
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ID_LIQUIDACION", rnd.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/ingreso/");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo_institucion.png"));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void reenviarCorreoDevolutiva(RegpNotaDevolutiva rnd) {
        try {
            if (admin || rnd.getElaborado().equalsIgnoreCase(session.getName_user())) {
                map = new HashMap();
                map.put("numTramiteRp", rnd.getTramite().getNumTramite());
                RegpLiquidacion liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                if (liquidacion != null) {
                    if (rnd.getAsunto().toUpperCase().contains("NEGATIVA")) {
                        as.generarFirmaEnviarCorreoNegativa(rnd, liquidacion, session.getName_user());
                    } else {
                        as.generarFirmaEnviarCorreoDevolutiva(rnd, liquidacion, session.getName_user());
                    }
                    JsfUti.messageInfo(null, "La devolutiva se firmará y se enviará por correo.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se puede firmar Devolutiva", "Porque debe de firmarlo el mismo usuario que lo elaboró");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public LazyModel<RegpNotaDevolutiva> getNotaDevolutivas() {
        return notaDevolutivas;
    }

    public void setNotaDevolutivas(LazyModel<RegpNotaDevolutiva> notaDevolutivas) {
        this.notaDevolutivas = notaDevolutivas;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
