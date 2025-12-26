/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.certificaciones;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.managedbeans.ConsultasRp;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PcRevisarDevolutivas extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private SeqGenMan sec;
    @Inject
    private OmegaUploader ou;
    @Inject
    private FirmaDigitalLocal fd;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private AsynchronousService as;
    @Inject
    private RegistroPropiedadServices rps;

    protected HashMap<String, Object> par;
    protected String observacion = "";
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpDocsTramite> docs = new ArrayList<>();
    protected Boolean hasDoc, online = false, tieneNotaDevolutiva = false;
    protected Boolean solicitudAprobada, caduco = false;
    protected RegpNotaDevolutiva notaDevolutiva;
    protected List<RegpNotaDevolutiva> notaDevolutivaAnalisis;
    protected List<Observaciones> observacionesTramites;
    protected Calendar cal;
    protected List<AclUser> asesorJuridico;
    protected AclUser us;
    protected String negativa;
    protected RegRegistrador registrador;
    protected Integer versionProceso = 0;
    protected File file;

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {

                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    hasDoc = true;
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    docs = itl.getDocumentosTramite(ht.getId());
                    if (this.getProcessInstanceAllAttachmentsFiles().isEmpty()) {
                        hasDoc = false;
                    }
                    if (liquidacion.getEstadoPago().getId() == 7L) {
                        online = true;
                    }
                    solicitudAprobada = Boolean.TRUE;

                    //nota devolutiva
                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});

                    if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                        tieneNotaDevolutiva = Boolean.TRUE;
                    }

                    observacionesTramites = rps.listarObservacionesPorTramite(ht);

                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    StringBuffer validarInscripcion = itl.validarInscripcion(liquidacion);
                    if (validarInscripcion != null) {
                        advertencias = validarInscripcion.toString();
                    }
                    cal = Calendar.getInstance();
                    //this.initDocEsc(this.ht.getNumTramite().toString());
                    /*map = new HashMap();
                    map.put("code", "CONTENIDO_NOTA_DEVOLUTIVA");
                    contenido = (ContenidoReportes) manager.findObjectByParameter(ContenidoReportes.class, map);*/
                    registrador = (RegRegistrador) manager.find(Querys.getRegRegistrador);
                    //registrador = (RegRegistrador) manager.find(Querys.getJuridico);
                    //caduco = this.validarRepertorio(liquidacion.getFechaRepertorio());
                    versionProceso = this.obtenerVersionProceso(this.getTaskDataByTaskID().getProcessDefinitionId());
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

    public void showDlgAprobado(Boolean estaAprobada) {
        this.solicitudAprobada = estaAprobada;
        if (solicitudAprobada) {
            observacion = "TAREA REALIZADA";
            completarTarea();
        } else {
            JsfUti.update("formObs");
            JsfUti.executeJS("PF('dlgObsvs').show();");
        }

    }

    public void rechazar() {
        try {
            this.solicitudAprobada = false;
            notaDevolutiva = new RegpNotaDevolutiva();
            notaDevolutiva.setTramite(ht);
            notaDevolutiva.setFechaIngreso(new Date());
            notaDevolutiva.setFecha(Constantes.canton + ", " + Utils.convertirFechaLetra(new Date()));
            notaDevolutiva.setRealizado(session.getName_user());
            //notaDevolutiva.setElaborado(us != null ? us.getUsuario() :  "");
            notaDevolutiva.setElaborado(session.getName_user());
            notaDevolutiva.setPara(ht.getSolicitante());
            //notaDevolutiva.setAsunto("Devolución de Trámite # " + ht.getNumTramite().toString() + datoAsunto);
            notaDevolutiva.setAsunto("Devolución de Trámite # " + ht.getNumTramite().toString());
            notaDevolutiva.setDetalle(Constantes.contenidoNotaDevolutiva);
            notaDevolutiva.setFirma(registrador.getNombreReportes());
            /*if (contenido != null && contenido.getValor() != null) {
                notaDevolutiva.setDetalle(contenido.getValor());
            } else {
                notaDevolutiva.setDetalle(Variables.contenidoNotaDevolutiva);
            }*/
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cancelar() {
        this.solicitudAprobada = true;
        notaDevolutiva = null;
    }

    public void guardarObservacion() {
        try {
            rps.guardarObservaciones(ht, session.getName_user(), observacion,
                    this.getTaskDataByTaskID().getName());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (observacion != null) {
                par = new HashMap<>();
                par.put("aprobado", solicitudAprobada ? 1 : 2);
                //par.put("entregaDocumentos", itl.getCandidateUserByRolName("entrega_documento"));
                par.put("jefe_inscripcion", itl.getUsuarioByRolName("jefe_inscripcion"));
                if (!solicitudAprobada) {
                    this.guardarNotaDevolutiva();
                    observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
                    //as.enviarCorreoTramiteNotaDevolutiva(liquidacion.getBeneficiario().getCorreo1(), liquidacion.getNumTramiteRp());
                }
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observacion.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void guardarNotaDevolutiva() {
        try {
            String anio = String.valueOf(cal.get(Calendar.YEAR));
            notaDevolutiva.setNumNotaDevolutiva(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaNotaDevolutiva).toString() + "-" + anio);
            RegpNotaDevolutiva result = (RegpNotaDevolutiva) manager.persist(notaDevolutiva);
            if (result != null) {
                this.imprimirNotaDevolutiva(result);
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void imprimirNotaDevolutiva(RegpNotaDevolutiva nd) {
        try {
            if (nd.getDocumento() == null) {
                ss.borrarDatos();
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("NotaDevolutiva");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("ID_NOTA", nd.getId());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + nd.getDocumento()
                        + "&name=" + nd.getNumNotaDevolutiva() + ".pdf&tipo=3&content=application/pdf");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, Messages.error, "");
        }
    }

    public void downloadDocHabilitante(RegpDocsTramite rdt) {
        try {
            if (rdt != null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + rdt.getDoc() + "&name=" + rdt.getNombreArchivo()
                        + "&tipo=3&content=" + rdt.getContentType());
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cropDocHabilitante(RegpDocsTramite rdt) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?id=" + rdt.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showFormularioOnline() {
        try {
            if (ht.getId() != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreReporte("FormularioOnline");
                ss.setNombreSubCarpeta("ingreso");
                ss.agregarParametro("ID_TRAMITE", ht.getId());
                ss.agregarParametro("FOTO1", this.getImage(Querys.getOid1Solicitud));
                ss.agregarParametro("FOTO2", this.getImage(Querys.getOid2Solicitud));
                ss.agregarParametro("FOTO3", this.getImage(Querys.getOid3Solicitud));
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
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

    public InputStream getImage(String sql) {
        BigInteger oid = (BigInteger) manager.getNativeQuery(sql, new Object[]{ht.getId()});
        if (oid != null) {
            return ou.streamFile(oid.longValue());
        }
        return null;
    }

    public void showDlgNegativa() {
        try {
            JsfUti.update("formNegativa");
            JsfUti.executeJS("PF('dlgNegativa').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarNegativa() {
        try {
            if (negativa != null && !negativa.isEmpty()) {
                liquidacion.setInfAdicional(negativa);
                liquidacion.setIngresado(Boolean.TRUE);
                manager.merge(liquidacion);
                observacion = "Pasa a inscribir negativa.";
                solicitudAprobada = true;
                this.completarTarea();
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el motivo de la NEGATIVA.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion="
                    + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void nuevaObservacion() {
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");
    }

    public void mantenerEstado() {
        this.guardarObservacion();
        this.continuar();
    }

    public void generarNuevoRepertorio() {
        try {
            observacion = "Cambio de repertorio, repertorio caducado: " + liquidacion.getRepertorio()
                    + ", fecha: " + liquidacion.getFechaRepertorio();
            if (rps.generarNuevoRepertorio(liquidacion)) {
                this.mantenerEstado();
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRepertorio() {
        if (caduco) {
            observacion = "Cambio de secuencia, repertorio caducado: " + liquidacion.getRepertorio()
                    + ", fecha de repertorio: " + liquidacion.getFechaRepertorio();
            if (rps.generarNuevoRepertorio(liquidacion)) {
                this.mantenerEstado();
            } else {
                JsfUti.messageError(null, Messages.error, "");
            }
        } else {
            JsfUti.messageWarning(null, "No se puede realizar esta accion.", "");
        }
    }

    public void completarTareaReasignar() {
        try {
            if (us != null && us.getId() != null) {
                observacion = "TAREA REALIZADA";
                par = new HashMap<>();
                par.put("inscriptor", us.getUsuario());
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el cambio de usuario.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showDlgRegresarJuridico() {
        JsfUti.update("formRegresar");
        JsfUti.executeJS("PF('dlgRegresar').show();");
    }

    public void completarTareaRegresarJuridico() {
        try {
            if (observacion != null) {
                par = new HashMap<>();
                par.put("aprobado", 2);
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observacion.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarNotaDevolutiva() {
        try {
            notaDevolutiva = notaDevolutivaAnalisis.get(0);
            if (notaDevolutiva.getDocumento() == null) {
                file = fd.firmarNotaDevolutiva(notaDevolutiva);
            } else {
                file = fd.descargarDevolutiva(notaDevolutiva);
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(file.getAbsolutePath());
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");

            JsfUti.update("formDevolutiva");
            JsfUti.executeJS("PF('dlgDevolutiva').show();");
        } catch (IOException e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaDevolutiva() {
        try {
            if (file.isFile()) {
                /*as.enviarCorreoNotaDevolutiva(liquidacion.getNumTramiteRp(), liquidacion.getBeneficiario().getCorreo1(),
                        file.getAbsolutePath(), session.getName_user());*/
                as.enviarCorreoNotaDevolutiva(liquidacion.getId(),file.getAbsolutePath(), session.getName_user());
                observacion = "Firma y envío de nota devolutiva nro. " + notaDevolutiva.getNumNotaDevolutiva();
                par = new HashMap<>();
                par.put("aprobado", 1);
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageError(null, "NO se puede encontrar archivo para el envío del correo.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<AclUser> getInscriptores() {
        return rps.getUsuariosByRolName("inscriptor");
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

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public Boolean getSolicitudAprobada() {
        return solicitudAprobada;
    }

    public void setSolicitudAprobada(Boolean solicitudAprobada) {
        this.solicitudAprobada = solicitudAprobada;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public Boolean getTieneNotaDevolutiva() {
        return tieneNotaDevolutiva;
    }

    public void setTieneNotaDevolutiva(Boolean tieneNotaDevolutiva) {
        this.tieneNotaDevolutiva = tieneNotaDevolutiva;
    }

    public List<RegpNotaDevolutiva> getNotaDevolutivaAnalisis() {
        return notaDevolutivaAnalisis;
    }

    public void setNotaDevolutivaAnalisis(List<RegpNotaDevolutiva> notaDevolutivaAnalisis) {
        this.notaDevolutivaAnalisis = notaDevolutivaAnalisis;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public String getNegativa() {
        return negativa;
    }

    public void setNegativa(String negativa) {
        this.negativa = negativa;
    }

    public Boolean getCaduco() {
        return caduco;
    }

    public void setCaduco(Boolean caduco) {
        this.caduco = caduco;
    }

    public AclUser getUs() {
        return us;
    }

    public void setUs(AclUser us) {
        this.us = us;
    }

    public Integer getVersionProceso() {
        return versionProceso;
    }

    public void setVersionProceso(Integer versionProceso) {
        this.versionProceso = versionProceso;
    }

}
