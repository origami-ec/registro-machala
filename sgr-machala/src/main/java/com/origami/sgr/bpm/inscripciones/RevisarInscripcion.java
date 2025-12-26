/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.inscripciones;

import com.origami.config.SisVars;
import com.origami.documental.lazy.LazyModelWS;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.services.DocumentalService;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclRol;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.Observaciones;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RegpPronunciamientoJuridico;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.restful.BalconServicios;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class RevisarInscripcion extends BpmManageBeanBaseRoot implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RevisarInscripcion.class.getName());

    @Inject
    private RegistroPropiedadServices reg;
    @Inject
    private SeqGenMan sec;
    @Inject
    private IngresoTramiteLocal itl;

    protected HashMap<String, Object> par;
    protected String observacion = "", mensaje = "";
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected List<RegpTareasTramite> tareas;
    protected List<Observaciones> observacionesTramites;
    protected RegRegistrador registrador;
    protected RegpTareasTramite rtt;
    protected AclUser user;
    protected RegpNotaDevolutiva notaDevolutiva;
    protected Boolean solicitudAprobada;
    protected Boolean certificador;
    protected CtlgItem tipo;
    protected AclRol rol;
    protected AclUser usuario;
    protected Integer version = 0;
    protected String jefe_inscripcion;

    protected Boolean tieneNotaDevolutiva = false, tienePronunciamiento = false;
    protected List<RegpNotaDevolutiva> notaDevolutivaAnalisis;
    protected List<RegpPronunciamientoJuridico> pronunciamientos;

    private LazyModelWS<ArchivoDocs> lazyArchivos;
    private ArchivoDocs archivo;

    @PostConstruct
    protected void iniView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    map = new HashMap();
                    map.put("numTramiteRp", tramite);
                    liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);
                    tareas = reg.getTareasTramite(ht.getId());
                    observacionesTramites = reg.listarObservacionesPorTramite(ht);
                    if (Utils.isEmpty(observacionesTramites)) {
                        observacionesTramites = new ArrayList();
                    }
                    map = new HashMap();
                    map.put("actual", Boolean.TRUE);
                    registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                    solicitudAprobada = Boolean.TRUE;
                    user = manager.find(AclUser.class, session.getUserId());
                    this.validaRoles();

                    StringBuffer validarInscripcion = itl.validarInscripcion(liquidacion);
                    if (validarInscripcion != null) {
                        advertencias = validarInscripcion.toString();
                    }
                    version = engine.getProcessInstanceById(ht.getIdProceso()).getProcessDefinitionVersion();
                    jefe_inscripcion = itl.getUsuarioByRolName("jefe_inscripcion");
                    notaDevolutivaAnalisis = manager.findAll(Querys.getNotaDevolutivaByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});
                    if (Utils.isNotEmpty(notaDevolutivaAnalisis)) {
                        tieneNotaDevolutiva = Boolean.TRUE;
                    }
                    pronunciamientos = manager.findAll(Querys.getPronunciamientosByTramite,
                            new String[]{"idTramite"}, new Object[]{ht.getId()});
                    if (Utils.isNotEmpty(pronunciamientos)) {
                        tienePronunciamiento = Boolean.TRUE;
                    }

                    // Traer documentos
                    lazyArchivos = new LazyModelWS<>(SisVars.urlOrigamiDocs + "misDocumentos?numTramite="
                            + ht.getNumTramite(), session.getToken());
                    lazyArchivos.setEntitiArray(ArchivoDocs[].class);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        //ID ROL CERTIFICADOR
        certificador = session.getRoles().contains(56L);
    }

    public void redirectFichaNueva() {
        if (this.fichasDisponibles()) {
            ss.instanciarParametros();
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("tramite", ht.getId());
            JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
        } else {
            JsfUti.messageWarning(null, "Ya no tiene Fichas disponibles para crear.", "");
        }
    }

    public boolean fichasDisponibles() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getFicha()) {
                tt.setFicha(true);
                manager.update(tt);
                return true;
            }
        }
        return false;
    }

    public void realizarTarea(RegpTareasTramite ta) {
        try {
            tipo = null;
            rtt = null;
            if (ta.getRealizado()) {
                JsfUti.messageWarning(null, "Tarea de trámite ya fue concluida.", "");
            } else {
                if (ta.getDetalle().getActo().getSolvencia()) {
                    rtt = ta;
                    JsfUti.update("formCertf");
                    JsfUti.executeJS("PF('dlgCertificado').show();");
                    /*ss.instanciarParametros();
                    ss.agregarParametro("tarea", ta.getId());
                    ss.agregarParametro("taskID", this.getTaskId());
                    session.setTaskID(this.getTaskId());
                    JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");*/
                } else {
                    ss.instanciarParametros();
                    ss.agregarParametro("tarea", ta.getId());
                    ss.agregarParametro("taskID", this.getTaskId());
                    ss.agregarParametro("habilitar", true);
                    session.setTaskID(this.getTaskId());
                    JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
                }
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void verMovimiento(RegpTareasTramite ta, Boolean esArchivo) {
        try {
            if (ta.getRevisado() == null || !ta.getRevisado()) {
                ss.instanciarParametros();
                ss.agregarParametro("tarea", ta.getId());
                ss.agregarParametro("taskID", this.getTaskId());
                ss.agregarParametro("habilitar", false);
                ss.agregarParametro("archivo", esArchivo);
                session.setTaskID(this.getTaskId());
                JsfUti.redirectFaces("/procesos/registro/inscribirRevision.xhtml");
            } else {
                JsfUti.messageError(null, "Tarea ya fue Revisada", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void verMovimientoArchivo(RegpTareasTramite ta) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("tarea", ta.getId());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("habilitar", false);
            ss.agregarParametro("archivo", true);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/inscribir.xhtml");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion() {
        try {
            if (observacion == null || observacion.isEmpty()) {
                reg.guardarObservaciones(ht, session.getName_user(), "TAREA REALIZADA", this.getTaskDataByTaskID().getName());
            } else {
                reg.guardarObservaciones(ht, session.getName_user(), observacion, this.getTaskDataByTaskID().getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTarea() {
        try {
            if (this.validarTareas()) {
                this.guardarObservacion();
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                par = new HashMap<>();
                par.put("aprobado", 1);
                par.put("revision", 1);
                par.put("asistente_registrador", itl.getUsuarioByRolName("asistente_registrador"));
                par.put("jefe_certificacion", itl.getUsuarioByRolName("jefe_certificacion"));
                par.put("jefe_inscripcion", itl.getUsuarioByRolName("jefe_inscripcion"));
                par.put("ventanilla", itl.getUsuarioByRolName("ventanilla"));
                this.completeTask(this.getTaskId(), par);
                this.continuar();
            } else {
                JsfUti.messageWarning(null, "Debe de realizar todas las tareas que le corresponden.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaRechazoJuridico() {
        try {
            par = new HashMap<>();
            this.guardarObservacion();
            par.put("aprobado", solicitudAprobada ? 1 : 2);
            par.put("secretaria", itl.getCandidateUserByRolName("secretario_registral")); //FIRMA INSCRIPCION
            par.put("entregaDocumentos", itl.getCandidateUserByRolName("entrega_documento"));
            if (!solicitudAprobada) {
                this.guardarNotaDevolutiva();
                observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
            }
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    private void guardarNotaDevolutiva() {
        try {
            Calendar cal = Calendar.getInstance();
            String anio = String.valueOf(cal.get(Calendar.YEAR));
            notaDevolutiva.setNumNotaDevolutiva(sec.getSecuenciaGeneralByAnio(Constantes.secuenciaNotaDevolutiva).toString() + "-" + anio);
            RegpNotaDevolutiva result = (RegpNotaDevolutiva) manager.persist(notaDevolutiva);
            if (result != null) {
                this.imprimirNotaDvolutiva(result.getId());
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirNotaDvolutiva(Long idNotaDevolutiva) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreReporte("NotaDevolutiva");
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("ID_NOTA", idNotaDevolutiva);
        ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
        ss.agregarParametro("REGISTRADOR", user.getEnte().getNombresApellidos()
                + Constantes.saltoLinea + Constantes.tituloCertificador);
        JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
    }

    public void completarTareaRevisionInscripcion(Boolean revisionAprobada) {
        try {
            if (revisionAprobada) {
                this.completarTareaRevisionRp(revisionAprobada);
            } else {
                JsfUti.update("formObs");
                JsfUti.executeJS("PF('dlgObsvs').show();");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void observacionCertificado() {
        JsfUti.update("formObsCertificados");
        JsfUti.executeJS("PF('dlgObsvsCertificados').show();");

    }

    public void guardarComentarioTramite() {
        try {
            if (observacion != null && !observacion.isEmpty()) {
                this.guardarObservacion();
                observacionesTramites = reg.listarObservacionesPorTramite(ht);
            } else {
                JsfUti.messageWarning(null, "Faltan Datos", "Debe ingresar una observación.");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void mantenerEstado() {
        this.guardarObservacion();
        this.continuar();
    }

    public void cambiarEstadoHT() {
        try {
            if (observacion != null && !observacion.isEmpty()) {
                ht.setIdProcesoTemp(observacion);
                manager.update(ht);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaRevisionRp(Boolean revisionAprobada) {
        try {
            par = new HashMap<>();
            if (revisionAprobada) {
                par.put("aprobado", 1);
                if (ht.getRevisor() != null) {
                    par.put("analistaInscripcion", ht.getRevisor().getUsuario());
                }
                this.guardarObservacion();
            } else {
                // RECHAZADA ENVIA A: ANALISIS y ACTUALIZA LOS CAMPOS DE REALIZADO Y REVISADO
                this.inactivarTareasRealizas();
                this.guardarObservacion();
                par.put("aprobado", 2);
            }
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    //FINALIZA LA TAREA DE ENTREGA DE TRAMITES - SE LA HACE XK EL CERTIFICADO SE ENVIA AL CORREO
    public void finalizarTarea(Long numTramite) {
        Map map1 = new HashMap();
        map1.put("numTramite", numTramite);
        HistoricoTramites ht1 = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map1);
        String taskId = reg.getTaskIdFromNumTramite(numTramite);
        map1 = new HashMap();
        map1.put("numTramiteRp", numTramite);
        RegpLiquidacion liq = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map1);
        String obs = "Se envio el documento al correo: "
                + (!liq.getSolicitante().getCorreo1().isEmpty() ? liq.getSolicitante().getCorreo1() : "") + " - " + liq.getSolicitante().getNombreCompleto();

        if (!taskId.isEmpty()) {
            reg.guardarObservaciones(ht1, Constantes.tramiteDescargado, obs, "Entrega Certificado");

            try {
                HashMap<String, Object> par1 = new HashMap<>();
                this.reasignarTarea(taskId, Constantes.tramiteDescargado);
                this.completeTask(taskId, par1);
            } catch (Exception ex) {
                Logger.getLogger(BalconServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (liq.getCertificadoSinFlujo()) {
                reg.guardarObservaciones(ht1, Constantes.tramiteDescargado, obs, Constantes.certificadoExpress);
            }
        }
    }

    public void completarTareaArchivo() {
        try {
            par = new HashMap<>();
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaInscripciones() {
        try {
            par = new HashMap<>();
            if (this.validaTareas()) { //TODAS LAS TAREAS
                par.put("revision", 2);
                mensaje = "Todos los documentos fueron realizados. La tarea pasa a firma de documentos.";
            } else {                    // FALTAN CERTIFICADOS
                par.put("revision", 1);
                map = new HashMap<>();
                map.put("nombre", "certificador");
                rol = (AclRol) manager.findObjectByParameter(AclRol.class, map);
                usuario = sec.getUserForTramite(rol.getId(), 1);
                par.put("certificador", usuario.getUsuario());
                mensaje = "Faltan de realizar documentos. Usuario certificador asignado: " + usuario.getUsuario().toUpperCase();
            }
            JsfUti.update("formMsjs");
            JsfUti.executeJS("PF('dlgMensajes').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void completarTareaJefe() {
        try {
            this.reasignarTarea(this.getTaskId(), session.getName_user());
            this.completeTask(this.getTaskId(), par);
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaTareas(Boolean isRevision) {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRealizado()) {
                return false;
            }
            if (isRevision) {
                if (!tt.getRevisado()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarTareasArchivo() {
        RegMovimiento movimiento;
        for (RegpTareasTramite tt : tareas) {
            movimiento = reg.getMovimientoFromTarea(tt.getId());
            if (movimiento.getNumTomo() == null) {
                return false;
            }
            if (movimiento.getNumTomo().length() <= 0) {
                return false;
            }
            if (movimiento.getFolioInicio() == null) {
                return false;
            }
            if (movimiento.getFolioFin() == null) {
                return false;
            }
        }
        return true;
    }

    public void generarInscripciones() {
        RegMovimiento movimiento;
        for (RegpTareasTramite tt : tareas) {
            movimiento = reg.getMovimientoFromTarea(tt.getId());
            if (movimiento != null) {
                reg.generarInscripcion(movimiento);
            }
            tt.setRevisado(Boolean.TRUE);
            tt.setFechaRevision(new Date());
            manager.merge(tt);
        }
        tareas = reg.getTareasTramite(ht.getId());
    }

    public void inactivarTareasRealizas() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getDetalle().getActo().getSolvencia()) {
                tt.setRealizado(Boolean.FALSE);
                tt.setRevisado(Boolean.FALSE);
                manager.persist(tt);
            }
        }
    }

    public void actaInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                ss.agregarParametro("USUARIO", session.getName_user());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("P_MOVIMIENTO", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.toUpperCase());
                ss.setNombreReporte("ActaInscripcion");
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                ss.setEncuadernacion(Boolean.FALSE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            } else {
                JsfUti.messageWarning(null, "ADVERTENCIA", "No se encontró el movimiento para el contrato.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void razonInscripcion() {
        try {
            RegMovimiento mov = reg.getMovimientoFromTarea(rtt.getId());
            if (mov != null) {
                ss.instanciarParametros();
                ss.agregarParametro("USUARIO", session.getName_user());
                ss.agregarParametro("ID_MOV", mov.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                ss.agregarParametro("REVISOR_LEGAL", mov.getUserCreador().getUsuario().toUpperCase());
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
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDocumentos(RegpTareasTramite tt) {
        rtt = tt;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void verActaInscripcion(RegpTareasTramite tt) {
        rtt = tt;
        this.actaInscripcion();
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
                    case "C07": //CERTIFICADO GENERAL 
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
            LOGGER.log(Level.SEVERE, null, e);
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
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosRazon(RegCertificado ce) {
        try {
            List<RegCertificadoMovimiento> rcm = (List<RegCertificadoMovimiento>) ce.getRegCertificadoMovimientoCollection();
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", rcm.get(0).getMovimiento().getId());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            //ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            //ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            //ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void llenarParametrosInforme(RegCertificado ce) {
        try {
            ss.instanciarParametros();
            ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("NOMBRE", ce.getBeneficiario());
            ss.agregarParametro("BUSQUEDA", ce.getLinderosRegistrales());
            ss.agregarParametro("ID_CERTIFICADO", ce.getId());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            ss.agregarParametro("SHOW_SIGN", true);
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("HEADER_URL", JsfUti.getRealPath("/resources/image/header.png"));
            ss.agregarParametro("FOOTER_URL", JsfUti.getRealPath("/resources/image/footer.png"));
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/watermark.png"));
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("FDCertificadoInformeBienes");
        } catch (Exception e) {
            System.out.println(e);
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
            notaDevolutiva.setElaborado(session.getName_user());
            notaDevolutiva.setPara(ht.getSolicitante());
            notaDevolutiva.setAsunto("Devolución de Trámite # " + ht.getNumTramite().toString());
            notaDevolutiva.setDetalle(Constantes.contenidoDevolutivaCertificado);
            notaDevolutiva.setFirma(user.getEnte().getNombresApellidos()
                    + Constantes.saltoLinea + Constantes.tituloCertificador);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cancelar() {
        this.solicitudAprobada = true;
        this.notaDevolutiva = null;
    }

    public void seleccionarCertificado() {
        try {
            if (tipo == null) {
                JsfUti.messageWarning(null, "Debe seleccionar el tipo de certificacion.", "");
                return;
            }
            ss.instanciarParametros();
            ss.agregarParametro("tarea", rtt.getId());
            ss.agregarParametro("tipo", tipo.getCodename());
            ss.agregarParametro("taskID", this.getTaskId());
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/registro/certificar.xhtml");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public List<CtlgItem> getTiposCertificados() {
        return manager.findAllEntCopy(Querys.getCtlgItemCertificados);
    }

    public void visualizaScann(Long transaccion) {
        try {
            JsfUti.redirectNewTab(SisVars.urlbase + "resources/dialog/cropOmegaDocs.xhtml?transaccion=" + transaccion + "&tramite=" + ht.getNumTramite());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public boolean validarTareas() {
        if (this.certificador) {
            for (RegpTareasTramite tt : tareas) {
                if (!tt.getRealizado()) {
                    return false;
                }
            }
            return true;
        } else {
            for (RegpTareasTramite tt : tareas) {
                if (!tt.getRealizado() && !tt.getDetalle().getActo().getSolvencia()) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean validaTareas() {
        for (RegpTareasTramite tt : tareas) {
            if (!tt.getRealizado()) {
                return false;
            }
        }
        return true;
    }

    public void completarTareaDevolutiva() {
        try {
            par = new HashMap<>();
            if (!solicitudAprobada) {
                this.guardarNotaDevolutiva();
                observacion = "NOTA DEVOLUTIVA: " + notaDevolutiva.getAsunto();
                //as.enviarCorreoTramiteNotaDevolutiva(liquidacion.getBeneficiario().getCorreo1(), liquidacion.getNumTramiteRp());
            }
            this.guardarObservacion();
            if (this.version > 2 && liquidacion.getCertificado()) {
                par.put("aprobado", 2);
                this.reasignarTarea(this.getTaskId(), session.getName_user());
                this.completeTask(this.getTaskId(), par);
            }
            this.continuar();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirNotaDevolutiva(RegpNotaDevolutiva dev) {
        try {
            ss.borrarDatos();
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreReporte("NotaDevolutiva");
            ss.setNombreSubCarpeta("registro");
            ss.agregarParametro("ID_NOTA", dev.getId());
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("USUARIO", session.getName_user());
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarPronunciamiento(Long idObjeto) {
        ss.borrarDatos();
        ss.instanciarParametros();
        ss.setTieneDatasource(true);
        ss.setNombreReporte("PronunciamientoJuridico");
        ss.setNombreSubCarpeta("registro");
        ss.agregarParametro("ID_NOTA", idObjeto);
        ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
        ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
        JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
    }

    public void visualizarDocumento(ArchivoDocs docs) {
        JsfUti.redirectNewTab(SisVars.urlbase + "documental/digitalizacion/viewerDocs.xhtml?archivoId=" + docs.getId());
    }

    public void descargarDocumento(ArchivoDocs doc) {
        try {
            //inline
            //JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/pdf/" + doc.getNombre());
            //attachment
            JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/document/" + doc.getNombre());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void controlCalidadInscripciones(RegpTareasTramite ta) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("tarea", ta.getId());
            ss.agregarParametro("taskID", this.getTaskId());
            ss.agregarParametro("habilitar", true);
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/inscripciones/completarDatosInscripcion.xhtml");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
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

    public List<RegpTareasTramite> getTareas() {
        return tareas;
    }

    public void setTareas(List<RegpTareasTramite> tareas) {
        this.tareas = tareas;
    }

    public List<Observaciones> getObservacionesTramites() {
        return observacionesTramites;
    }

    public void setObservacionesTramites(List<Observaciones> observacionesTramites) {
        this.observacionesTramites = observacionesTramites;
    }

    public RegpTareasTramite getRtt() {
        return rtt;
    }

    public void setRtt(RegpTareasTramite rtt) {
        this.rtt = rtt;
    }

    public RegpNotaDevolutiva getNotaDevolutiva() {
        return notaDevolutiva;
    }

    public void setNotaDevolutiva(RegpNotaDevolutiva notaDevolutiva) {
        this.notaDevolutiva = notaDevolutiva;
    }

    public Boolean getSolicitudAprobada() {
        return solicitudAprobada;
    }

    public void setSolicitudAprobada(Boolean solicitudAprobada) {
        this.solicitudAprobada = solicitudAprobada;
    }

    public CtlgItem getTipo() {
        return tipo;
    }

    public void setTipo(CtlgItem tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getTieneNotaDevolutiva() {
        return tieneNotaDevolutiva;
    }

    public void setTieneNotaDevolutiva(Boolean tieneNotaDevolutiva) {
        this.tieneNotaDevolutiva = tieneNotaDevolutiva;
    }

    public Boolean getTienePronunciamiento() {
        return tienePronunciamiento;
    }

    public void setTienePronunciamiento(Boolean tienePronunciamiento) {
        this.tienePronunciamiento = tienePronunciamiento;
    }

    public List<RegpNotaDevolutiva> getNotaDevolutivaAnalisis() {
        return notaDevolutivaAnalisis;
    }

    public void setNotaDevolutivaAnalisis(List<RegpNotaDevolutiva> notaDevolutivaAnalisis) {
        this.notaDevolutivaAnalisis = notaDevolutivaAnalisis;
    }

    public List<RegpPronunciamientoJuridico> getPronunciamientos() {
        return pronunciamientos;
    }

    public void setPronunciamientos(List<RegpPronunciamientoJuridico> pronunciamientos) {
        this.pronunciamientos = pronunciamientos;
    }

    public LazyModelWS<ArchivoDocs> getLazyArchivos() {
        return lazyArchivos;
    }

    public void setLazyArchivos(LazyModelWS<ArchivoDocs> lazyArchivos) {
        this.lazyArchivos = lazyArchivos;
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

}
