/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.managedbeans;

import com.origami.config.SisVars;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class RevisionFirmaRp extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private RegistroPropiedadServices reg;

    @Inject
    private IngresoTramiteLocal itl;

    @Inject
    private AsynchronousService as;

    protected HashMap<String, Object> par;
    protected String formatoArchivos;
    protected String observacion;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected Boolean hasDoc, online = false;
    protected RegRegistrador registrador;
    protected AclUser user;
    protected RegpTareasTramite rtt;
    private List<Observaciones> observacionesTramites;
    //protected List<RegCertificado> certificados;

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    hasDoc = false;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }
                    
                    observacionesTramites = reg.listarObservacionesPorTramite(ht);
                    tareas = reg.getTareasTramite(ht.getId());
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            this.guardarObservacion();
            par = new HashMap<>();
            par.put("ventanilla", itl.getCandidateUserByRolName("ventanilla"));
            //as.enviarCorreoTramiteFinalizado(liquidacion);
            //as.generarFirmaDigital(ht.getNumTramite());
            as.firmarDocumentosEnviarCorreo(liquidacion, session.getName_user());
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion() {
        try {
            if (observacion != null) {
                if (observacion.isEmpty()) {
                    observacion = this.getTaskDataByTaskID().getName();
                }
                Observaciones ob = new Observaciones();
                ob.setObservacion(observacion);
                ob.setEstado(true);
                ob.setFecCre(new Date());
                ob.setIdTramite(ht);
                /*if (this.getTaskDataByTaskID() == null) {
                    JsfUti.messageError(null, "Error", "Ingrese de nuevo, por la bandeja de tareas.");
                    return;
                }*/
                ob.setTarea(this.getTaskDataByTaskID().getName());
                ob.setUserCre(session.getName_user());
                manager.persist(ob);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pasarDevolutiva() {
        try {
            par = new HashMap<>();
            par.put("catastrar", 3);
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo() + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadDocument() {
        try {
            BigInteger oid = (BigInteger) manager.getNativeQuery(Querys.getOidSolicitud, new Object[]{ht.getId()});
            if (oid != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + oid + "&name=DocumentOnline.pdf&tipo=3&content=application/pdf");
            } else {
                JsfUti.messageWarning(null, "El usuario no adjuntó el documento en línea.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocumentos(RegpTareasTramite tt) {
        rtt = tt;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void actaInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                ss.agregarParametro("P_MOVIMIENTO", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.setNombreReporte("ActaInscripcion");
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void razonInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                if (user != null && user.getEnte() != null) {
                    ss.agregarParametro("USUARIO", user.getEnte().getNombreCompleto().toUpperCase());
                }
                String juridico = itl.getUsuarioByRolName("director_registral");
                String jefe_inscripcion = itl.getUsuarioByRolName("jefe_inscripcion");
                ss.agregarParametro("ID_MOV", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("REVISOR_LEGAL", juridico.toUpperCase());
                ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("RazonInscripcion");
                ss.setNombreSubCarpeta("registro");
                ss.setTieneDatasource(true);
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarCertificado() {
        try {
            RegCertificado ce = reg.getCertificadoFromTarea(rtt.getId());
            if (ce != null) {
                this.llenarParametros(ce);
                switch (ce.getTipoDocumento()) {
                    case "C01": //NO POSEER BIENES
                        ss.setNombreReporte("CertificadoNoBienes");
                        break;
                    case "C02": //CERTIFICADO DE SOLVENCIA
                        ss.setNombreReporte("CertificadoSolvencia");
                        break;
                    case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                        ss.setNombreReporte("CertificadoHistoriaDominio");
                        break;
                    case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                        ss.setNombreReporte("CertificadoFichaPersonal");
                        break;
                    case "C05": //CERTIFICADO MERCANTIL 
                        ss.setNombreReporte("CertificadoMercantil");
                        break;
                    case "C06": //CERTIFICADO DE RAZON DE INSCRIPCION
                        this.llenarParametrosRazon(ce);
                        break;
                    case "C07": //CERTIFICADO MERCANTIL 
                        ss.setNombreReporte("CertificadoGeneral");
                        break;
                    default:
                        JsfUti.messageInfo(null, "No se pudo visualizar el certificado.", "");
                        return;
                }
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el certificado para el contrato.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametros(RegCertificado ce) {
        try {
            user = manager.find(AclUser.class, session.getUserId());
            map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("EMISION", ce.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", ce.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", ce.getUsoDocumento());
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("FDCopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion=" + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public String getFormatoArchivos() {
        return formatoArchivos;
    }

    public void setFormatoArchivos(String formatoArchivos) {
        this.formatoArchivos = formatoArchivos;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public List<RegpDocsTramite> getDocs() {
        return docs;
    }

    public void setDocs(List<RegpDocsTramite> docs) {
        this.docs = docs;
    }

    public Boolean getHasDoc() {
        return hasDoc;
    }

    public void setHasDoc(Boolean hasDoc) {
        this.hasDoc = hasDoc;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    /*public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }*/
    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public RegpTareasTramite getRtt() {
        return rtt;
    }

    public void setRtt(RegpTareasTramite rtt) {
        this.rtt = rtt;
    }

}
