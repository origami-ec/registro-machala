/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental;

import com.origami.config.SisVars;
import com.origami.documental.models.YuraConsultaRPM;
import com.origami.documental.models.YuraModel;
import com.origami.documental.models.YuraRespuestaRPM;
import com.origami.documental.services.DocumentalService;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ConsultaDocs implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConsultaDocs.class.getName());

    @Inject
    private DocumentalService service;

    private YuraConsultaRPM consulta;
    private YuraRespuestaRPM respuesta;
    private SimpleDateFormat sdf;
    private Date fechaConsulta;
    private YuraModel yuraModel;

    private Boolean consultar;
    private Integer repertorio;
    private Long fecha;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }

    protected void initView() {
        try {
            consultar = Boolean.TRUE;
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            consulta = new YuraConsultaRPM();
            if (repertorio != null && fecha != null) {
                consultar = Boolean.FALSE;
                consulta.setNumeroRepertorio(repertorio);
                consulta.setFechaInscripcion(sdf.format(new Date(fecha)));
                this.busquedaPorParametros();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void busquedaPorParametros() {
        try {
            //respuesta = service.consultaDocumental(consulta);
            if (respuesta.getListaItemRespuestaRPMDTO() == null) {
                JsfUti.messageError(null, respuesta.getMensaje(), "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void realizarBusqueda() {
        try {
            if (this.fechaConsulta != null) {
                consulta.setFechaInscripcion(sdf.format(this.fechaConsulta));
            } else {
                consulta.setFechaInscripcion(null);
            }
            if (consulta.getLibro().isEmpty()) {
                consulta.setLibro(null);
            }
            if (consulta.getTomo().isEmpty()) {
                consulta.setTomo(null);
            }
            if (consulta.getIdentificacion1().isEmpty()) {
                consulta.setIdentificacion1(null);
            }
            if (consulta.getIdentificacion2().isEmpty()) {
                consulta.setIdentificacion2(null);
            }
            if (consulta.getNumeroRepertorio() == null && consulta.getAnioRepertorio() == null
                    && consulta.getNumeroInscripcion() == null && consulta.getFechaInscripcion() == null
                    && consulta.getLibro() == null && consulta.getTomo() == null
                    && consulta.getIdentificacion1() == null && consulta.getIdentificacion2() == null) {
                JsfUti.messageWarning(null, "Debe ingresar al menos un criterio para la búsqueda.", "");
                return;
            }
            //respuesta = service.consultaDocumental(consulta);
            if (respuesta.getListaItemRespuestaRPMDTO() == null) {
                JsfUti.messageError(null, respuesta.getMensaje(), "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarDocumento(YuraModel model) {
        try {
            JsfUti.redirectNewTab(model.getUrlDescargaDocumento());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectModel(YuraModel model) {
        try {
            yuraModel = model;
            JsfUti.update("formConsulta");
            JsfUti.executeJS("PF('dlgConsulta').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizarDocumento(YuraModel model) {
        String archivo = null;
        try {
            //archivo = service.descargarArchivo(model.getUrlDescargaDocumento());
            if (archivo != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/visorPdf.xhtml?uuid=" + archivo);
            } else {
                JsfUti.messageError(null, "Error al descargar el documento.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgVisor(YuraModel model) {
        try {
            yuraModel = model;
            JsfUti.update("formVisor");
            JsfUti.executeJS("PF('dlgVisor').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public YuraConsultaRPM getConsulta() {
        return consulta;
    }

    public void setConsulta(YuraConsultaRPM consulta) {
        this.consulta = consulta;
    }

    public YuraRespuestaRPM getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(YuraRespuestaRPM respuesta) {
        this.respuesta = respuesta;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public YuraModel getYuraModel() {
        return yuraModel;
    }

    public void setYuraModel(YuraModel yuraModel) {
        this.yuraModel = yuraModel;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public Boolean getConsultar() {
        return consultar;
    }

    public void setConsultar(Boolean consultar) {
        this.consultar = consultar;
    }

}
